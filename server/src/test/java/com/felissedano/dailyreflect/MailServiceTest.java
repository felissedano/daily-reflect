package com.felissedano.dailyreflect;

import com.felissedano.dailyreflect.common.service.MailService;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.*;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MailServiceTest {


    static GreenMail greenMail = new GreenMail(ServerSetup.SMTP.port(3025))
            .withConfiguration(new GreenMailConfiguration().withUser("admin@example.com", "password"));


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailService mailService;

    @BeforeEach
    void setup() {
        greenMail.start();
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
    }

    @Test
    void whenSendAnEmail_shouldReceiveAndWithCorrectContent() {

        // Create a simple email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("john@example.com");
        message.setSubject("Hello World!");
        message.setText("Some content.");


        // Send the email
        mailSender.send(message);

        // Verify the email was received
        MimeMessage received = greenMail.getReceivedMessages()[0];
        try {
            assertThat(received.getSubject()).contains("Hello World!");
            assertThat(received.getContent().toString()).contains("Some content.");
            assertThat(received.getAllRecipients()[0].toString()).contains("john@example.com");
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void whenSendMessageOfAnotherLocaleWithEmailService_shouldReceiveWithCorrectLanguage() {
        mailService.sendTextEmail("john@example.com", "general.greetings.hello", "general.hello-world", null, Locale.FRENCH);
        MimeMessage received = greenMail.getReceivedMessages()[0];
        try {
            assertThat(received.getSubject()).contains("Bonjour");
            assertThat(received.getContent().toString()).contains("Bonjour World!");
            assertThat(received.getAllRecipients()[0].toString()).contains("john@example.com");

        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }


    }

}
