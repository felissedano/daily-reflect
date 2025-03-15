package com.felissedano.dailyreflect.auth.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String user(Authentication auth) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
//
//        // Log the authentication details for debugging
        if (authentication != null) {
            System.out.println("Authenticated user: " + authentication.getName());
            System.out.println("Authorities: " + authentication.getAuthorities());
        } else {
            System.out.println("No authentication found in SecurityContext");
        }
       return "Hello " + auth.getName() + " with authorities: " + auth.getAuthorities();
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin(Authentication auth) {
        return "Hello admin " + auth.getName() + " with authorities: " + auth.getAuthorities();
    }
}
