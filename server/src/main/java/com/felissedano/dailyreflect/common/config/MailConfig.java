package com.felissedano.dailyreflect.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
//@ConfigurationProperties(prefix = "spring.mail")
//@ConfigurationProperties
public class MailConfig {



    @Value("${spring.mail.protocol}")
    String protocol;

    @Value("${spring.mail.port}")
    int port;

    @Value("${spring.mail.host}")
    String host;

    @Value("${spring.mail.username}")
    String username;

    @Value("${spring.mail.password}")
    String password;

    @Bean
    JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(this.host);
        javaMailSender.setPort(this.port);
        javaMailSender.setProtocol(this.protocol);
        javaMailSender.setUsername(this.username);
        javaMailSender.setPassword(this.password);

        return javaMailSender;
    }


}

