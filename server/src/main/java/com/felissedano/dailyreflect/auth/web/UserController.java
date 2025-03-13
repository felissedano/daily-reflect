package com.felissedano.dailyreflect.auth.web;

import org.springframework.web.bind.annotation.GetMapping;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class UserController {

    @GetMapping("/user")
    public String user() {
        return "Hello World!";
    }
}
