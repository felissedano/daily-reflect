package com.felissedano.dailyreflect.journaling;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.felissedano.dailyreflect.TestContainerConfiguration;
import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.domain.repository.UserRepository;
import com.felissedano.dailyreflect.auth.service.UserService;
import com.felissedano.dailyreflect.auth.service.dto.UserDto;
import com.felissedano.dailyreflect.auth.web.AuthController;
import com.felissedano.dailyreflect.common.service.MailService;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfiguration.class)
@ActiveProfiles("test")
public class JournalIntegrationTest {

    @MockitoBean
    MailService mailService;

    @Autowired
    JournalRepository journalRepository;

    @LocalServerPort
    private Integer serverPort;

    @Autowired
    AuthController authController;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    User user = null;

    @BeforeEach
    public void setup() {
        RestAssured.port = this.serverPort;
        if (user == null) {
            UserDto userDto = new UserDto("journal@integration.test", "journalintegration", "password");
            User regUser = userService.registerNormalUser(userDto);
            regUser.setEnabled(true);
            user = userRepository.save(regUser);
        }
    }

    @AfterEach
    public void tearDown() {
    }

    private String getXsrfToken() {
        ExtractableResponse<Response> request = when().get("api/auth/is-auth")
                .then().statusCode(200).extract();

        return request.cookie("XSRF-TOKEN");
    }

    @Test
    public void testCanGetUserProfileOnEachRequest() {

        // given
        // User user = new User("jounalintegration", "journal@integration.test",
        // passwordEncoder.encode("password"));
        // user.setEnabled(true);
        // userService.registerNormalUser(user);

        String xsrfToken = getXsrfToken();

        ExtractableResponse<Response> request = given()
                .contentType(ContentType.JSON)
                .header(new Header("X-XSRF-TOKEN", xsrfToken)).cookie("XSRF-TOKEN", xsrfToken)
                .body("""
                        {
                          "email": "journal@integration.test",
                          "password": "password"
                        }
                        """)
                .when().post("api/auth/login")
                .then().statusCode(201).extract();

        var sessionId = request.sessionId();
        // var xsrfToken = request.cookie("XSRF-TOKEN");

        // verify login is successful
        given().sessionId(sessionId).when().get("api/journal/mock/hellothere").then()
                .body(containsString("hellothere"));
    }

}
