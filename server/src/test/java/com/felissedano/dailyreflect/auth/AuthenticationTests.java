package com.felissedano.dailyreflect.auth;

import com.felissedano.dailyreflect.TestContainerConfiguration;
import com.felissedano.dailyreflect.auth.dtos.UserDto;
import com.felissedano.dailyreflect.auth.models.User;
import com.felissedano.dailyreflect.auth.models.enums.VerificationState;
import com.felissedano.dailyreflect.auth.services.UserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestContainerConfiguration.class)
@ActiveProfiles("test")
public class AuthenticationTests {

    @LocalServerPort
    private Integer serverPort;

    @Autowired
    AuthController authController;

    @Autowired
    UserService userService;

    @BeforeEach
    public void setup() {
        RestAssured.port = this.serverPort;
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
    public void whenUserLoginWithCorrectCredentialsButUnverified_shouldShowLocked(){

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
        when().post("api/auth/verify-email?email=joe@example.com&code="+code)
                .then()
                .body(containsString(VerificationState.VERIFICATION_SUCCESS.name()))
                .statusCode(201);

        when().post("api/auth/verify-email?email=joe@example.com&code="+code)
                .then()
                .body(containsString(VerificationState.ALREADY_VERIFIED.name()))
                .statusCode(404);

    }

    @Test
    public void whenUserVerifyEmailWithIncorrectCode_shouldFail() {
        User user = userService.registerNormalUser(new UserDto("eve@example.com", "eve1", "password"));
        String code = user.getVerificationCode();
        when().post("api/auth/verify-email?email=eve@example.com&code="+code+"wrong")
                .then()
                .body(containsString(VerificationState.TOKEN_NOT_MATCH.name()))
                .statusCode(404);

    }
}
