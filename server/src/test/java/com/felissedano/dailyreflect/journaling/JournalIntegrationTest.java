package com.felissedano.dailyreflect.journaling;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.felissedano.dailyreflect.TestContainerConfiguration;
import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.domain.repository.UserRepository;
import com.felissedano.dailyreflect.auth.service.UserService;
import com.felissedano.dailyreflect.auth.service.dto.UserDto;
import com.felissedano.dailyreflect.auth.web.AuthController;
import com.felissedano.dailyreflect.common.service.MailService;
import com.felissedano.dailyreflect.profile.Profile;
import com.felissedano.dailyreflect.profile.ProfileRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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
    ProfileRepository profileRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static User user = null;

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
    public void tearDown() {}

    private String getXsrfToken() {
        ExtractableResponse<Response> request =
                when().get("api/auth/is-auth").then().statusCode(200).extract();

        return request.cookie("XSRF-TOKEN");
    }

    private AuthResult loginUserForTest() {
        String xsrfToken = getXsrfToken();

        ExtractableResponse<Response> request = given().contentType(ContentType.JSON)
                .header(new Header("X-XSRF-TOKEN", xsrfToken))
                .cookie("XSRF-TOKEN", xsrfToken)
                .body(
                        """
                        {
                          "email": "journal@integration.test",
                          "password": "password"
                        }
                        """)
                .when()
                .post("api/auth/login")
                .then()
                .statusCode(201)
                .extract();

        String sessionId = request.sessionId();

        return new AuthResult(xsrfToken, sessionId);
    }

    private Date getDateFromString(String dateString, boolean inputHasNoTime) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String timePortion = inputHasNoTime ? "T00:00:00.000+00:00" : "";
        return sdf.parse(dateString + timePortion);
    }

    @Test
    public void tmp_testCanSaveMockJournal() {
        AuthResult authResult = loginUserForTest();

        // verify login is successful
        given().sessionId(authResult.sessionId)
                .when()
                .get("api/journal/mock/hellothere")
                .then()
                .body(containsString("hellothere"));
    }

    @Test
    public void whenUserCreateOrUpdateJournal_shouldSucceed() throws Exception {
        AuthResult authResult = loginUserForTest();

        given().sessionId(authResult.sessionId)
                .contentType(ContentType.JSON)
                .header(new Header("X-XSRF-TOKEN", authResult.xsrfToken()))
                .cookie("XSRF-TOKEN", authResult.xsrfToken())
                .body(
                        """
                {
                    "content": "HELLO!!!",
                    "tags": ["tag1", "tag2", "tag3"],
                    "date": "2025-01-01"
                }

                """)
                .when()
                .post("api/journal/edit")
                .then()
                .statusCode(201);

        Profile profile = profileRepository.findByUserEmail(user.getEmail()).get();
        Date date = getDateFromString("2025-01-01", true);

        Journal journal = journalRepository
                .findByDateAndProfile(getDateFromString("2025-01-01", true), profile)
                .get();
        assertThat(journal.getDate()).hasSameTimeAs(date);
        assertThat(journal.getContent()).isEqualTo("HELLO!!!");

        // Test update journal works
        given().sessionId(authResult.sessionId)
                .contentType(ContentType.JSON)
                .header(new Header("X-XSRF-TOKEN", authResult.xsrfToken()))
                .cookie("XSRF-TOKEN", authResult.xsrfToken())
                .body(
                        """
                {
                    "content": "New Content",
                    "tags": ["new tag", "tag3"],
                    "date": "2025-01-01"
                }

                """)
                .when()
                .post("api/journal/edit")
                .then()
                .statusCode(201);

        Journal journal2 = journalRepository.findById(journal.getId()).get();
        assertThat(journal2.getContent()).isEqualTo("New Content");
        assertThat(journal2.getTags()).isEqualTo(Arrays.asList("new tag", "tag3"));
    }

    @Test
    public void whenUserGetByDateOfExistingJournal_shouldSucceed(){
        AuthResult authResult = loginUserForTest();

        given().sessionId(authResult.sessionId)
                .contentType(ContentType.JSON)
                .header(new Header("X-XSRF-TOKEN", authResult.xsrfToken()))
                .cookie("XSRF-TOKEN", authResult.xsrfToken())
                .body(
                        """
                {
                    "content": "A brand new journal",
                    "tags": ["a tag"],
                    "date": "2025-01-02"
                }

                """)
                .when()
                .post("api/journal/edit")
                .then()
                .statusCode(201);

        given().sessionId(authResult.sessionId()).when()
            .get("api/journal/date/2025-01-02").then().statusCode(200).body("content", containsString("A brand new journal")).body("date", containsString("2025-01-02"));
    }

    private record AuthResult(String xsrfToken, String sessionId) {}
}

