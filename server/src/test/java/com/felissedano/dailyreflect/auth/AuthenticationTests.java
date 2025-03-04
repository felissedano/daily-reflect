package com.felissedano.dailyreflect.auth;

import com.felissedano.dailyreflect.TestContainerConfiguration;
import com.felissedano.dailyreflect.auth.dtos.UserDto;
import com.felissedano.dailyreflect.auth.models.User;
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
import org.testcontainers.junit.jupiter.Testcontainers;

import java.awt.*;

import static org.assertj.core.api.Assertions.*;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
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

    @Test void whenUserLoginWithCorrectCredentialsButUnverified_shouldShowLocked(){

        userService.registerNormalUser(new UserDto("john@example.com", "john", "password"));
        var user = userService.findUserByEmail("john@example.com");
        assertThat(user.isPresent());
        System.out.println(user.get().getPassword());

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                        "email":  "john@example.com",
                        "password":  "password"
                        }
                        """)
                .when().post("/api/auth/login")
                .then().statusCode(423)
                .body(containsString("Account is disabled"));
    }
}
