package com.felissedano.dailyreflect;

import com.felissedano.dailyreflect.common.service.MailService;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

// TODO should probably use production profile with production DB when doing this specific Integration Test
@SpringBootTest
@Import(TestContainerConfiguration.class)
@ActiveProfiles("test")
class DailyReflectApplicationTest {

	static GreenMail greenMail = new GreenMail(ServerSetup.SMTP.port(3025))
			.withConfiguration(new GreenMailConfiguration().withUser("admin@example.com", "password"));

	@BeforeEach
	void setup() {
		greenMail.start();
	}

	@AfterEach
	void tearDown() {
		greenMail.stop();
	}

	@Test
	void contextLoads() {
	}

}
