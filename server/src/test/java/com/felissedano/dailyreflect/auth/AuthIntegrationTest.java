package com.felissedano.dailyreflect.auth;

import com.felissedano.dailyreflect.TestContainerConfiguration;
import com.felissedano.dailyreflect.auth.service.dto.UserDto;
import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.service.UserService;
import com.felissedano.dailyreflect.auth.web.AuthController;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestContainerConfiguration.class)
@ActiveProfiles("test")
public class AuthIntegrationTest {

    @LocalServerPort
    private Integer serverPort;

    @Autowired
    AuthController authController;

    @Autowired
    UserService userService;

    static GreenMail greenMail = new GreenMail(ServerSetup.SMTP.port(3025))
            .withConfiguration(new GreenMailConfiguration().withUser("admin@example.com", "password"));


    @BeforeEach
    public void setup() {
        RestAssured.port = this.serverPort;
        greenMail.start();
    }

    @AfterEach
    public void tearDown() {
        greenMail.stop();
    }

    @Test
    public void whenUserRegisterWithValidCredential_shouldSucceed() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "email": "john@example.com",
                          "username": "john",
                          "password": "password"
                        }
                        """)
                .when().post("api/auth/register")
                .then().statusCode(201)
                .body("enabled", equalTo(false))
                .body("email", equalTo("john@example.com"));

    }

    @Test
    public void whenUserLoginWithIncorrectCredentials_shouldNotSucceed() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                        "email":  "fake@example.com",
                        "password":  "password"}
                        """)
                .when().post("/api/auth/login")
                .then().statusCode(401)
                .body(containsString("Email or password incorrect"));
    }

    @Test
    public void whenUserLoginWithCorrectCredentialsButUnverified_shouldShowLocked() {

        userService.registerNormalUser(new UserDto("jane@example.com", "jane", "password"));

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                        "email":  "jane@example.com",
                        "password":  "password"
                        }
                        """)
                .when().post("/api/auth/login")
                .then().statusCode(423)
                .body(containsString("Account is disabled"));
    }

    @Test
    public void whenUserVerifyEmailWithCorrectCodeButDoItAgain_shouldSucceedAndThenFail() {
        User user = userService.registerNormalUser(new UserDto("joe@example.com", "joe1", "password"));
        String code = user.getVerificationCode();
        when().post("api/auth/verify-email?email=joe@example.com&code=" + code)
                .then()
                .body(containsString("Verification Success"))
                .statusCode(201);

        when().post("api/auth/verify-email?email=joe@example.com&code=" + code)
                .then()
                .body(containsString("Email Verification Failed"))
                .body("type", containsString("/problems/auth/token-expired-or-invalid"))
                .statusCode(400);

    }

    @Test
    public void whenUserVerifyEmailWithIncorrectCode_shouldFail() {
        User user = userService.registerNormalUser(new UserDto("eve@example.com", "eve1", "password"));
        String code = user.getVerificationCode();
        when().post("api/auth/verify-email?email=eve@example.com&code=" + code + "wrong")
                .then()
                .body("type", containsString("/problems/auth/token-expired-or-invalid"))
                .body(containsString("Email Verification Failed"))
                .body(containsString("The link is expired or invalid"))
                .statusCode(400);

    }

    @Test
    public void whenUserVerifyEmailWithIncorrectCodeWithLocale_shouldFailWithLocalResponse() {
        User user = userService.registerNormalUser(new UserDto("bob@example.com", "bob1", "password"));
        String code = user.getVerificationCode();
        given()
                .header(new Header("Accept-Language", "fr"))
                .when().post("api/auth/verify-email?email=bob@example.com&code=" + code + "wrong")
                .then()
                .body("type", containsString("/problems/auth/token-expired-or-invalid"))
                .body("title", containsString("Email Verification Failed"))
                .body("detail", containsString("Le lien est expiré ou invalide"))
                .statusCode(400);

    }

    @Test
    public void whenUserRegisterVerifyAndLogin_shouldSucceed() {
        UserDto user = new UserDto("alice@example.com", "alice", "password");
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "email": "alice@example.com",
                            "username": "alice",
                            "password": "password"
                        }
                        """)
                .when().post("api/auth/register")
                .then()
                .statusCode(201);

        List<MimeMessage> list = Arrays.stream(greenMail.getReceivedMessages()).filter(mimeMessage -> {
            try {
                return mimeMessage.getAllRecipients()[0].toString().equals("alice@example.com");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        assertThat(list.size()).isEqualTo(1);

        String verificationToken = userService.findUserByEmail("alice@example.com").orElseThrow().getVerificationCode();

        // verify email
        when().post("api/auth/verify-email?email=alice@example.com&code=" + verificationToken)
                .then()
                .body(containsString("Verification Success"))
                .statusCode(201);

        // login
        var sessionId = given()
                .contentType(ContentType.JSON).body("""
                        {
                          "email": "alice@example.com",
                          "password": "password"
                        }
                        """)
                .when().post("api/auth/login")
                .then().statusCode(201).extract().sessionId();


        // verify login is successful
       given().sessionId(sessionId).when().get("api/user/hello").then().body(containsString("Hello World!"));

       // logout
        given().sessionId(sessionId).post("api/auth/logout").then().statusCode(200);

        // verify cannot access secure endpoint
        given().sessionId(sessionId).get("api/user/hello").then().statusCode(401);
    }

}
