package com.felissedano.dailyreflect.auth.service;

import com.felissedano.dailyreflect.DailyReflectApplication;
import com.felissedano.dailyreflect.TestContainerConfiguration;
import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;


@Testcontainers
@Import(TestContainerConfiguration.class)
@SpringBootTest(
        classes = DailyReflectApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
//@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTests {

//    @Container
//    @ServiceConnection
//    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17.2")
//            .withDatabaseName("test_db")
//            .withUsername("user")
//            .withPassword("password");

//    @DynamicPropertySource
//    public static void setDataSourceProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", () -> postgresContainer.getJdbcUrl());
//        registry.add("spring.datasource.username", () -> postgresContainer.getUsername());
//        registry.add("spring.datasource.password", () -> postgresContainer.getPassword());
//    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll(); // Clear the database before each test
    }

    @AfterEach
    public void tearDown() {
//        userRepository.deleteAll();
//        postgresContainer.stop();
    }

    @Test
    public void testSaveUser() {
        User user = new User("John Doe", "john.doe@example.com", "password");
        userService.save(user);

        List<User> users = userService.findAll();
        assertEquals(1, users.size());
        assertEquals("John Doe", users.getFirst().getUsername());
    }

}