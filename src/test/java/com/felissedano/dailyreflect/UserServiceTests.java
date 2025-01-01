package com.felissedano.dailyreflect;

import com.felissedano.dailyreflect.models.User;
import com.felissedano.dailyreflect.repositories.UserRepository;
import com.felissedano.dailyreflect.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;


@Testcontainers
@SpringBootTest(
        classes = DailyReflectApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTests {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17.2")
            .withDatabaseName("test_db")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    public static void setDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgresContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgresContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgresContainer.getPassword());
    }

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
        User user = new User("John Doe", "john.doe@example.com");
        userService.save(user);

        List<User> users = userService.findAll();
        assertEquals(1, users.size());
        assertEquals("John Doe", users.getFirst().getName());
    }

}