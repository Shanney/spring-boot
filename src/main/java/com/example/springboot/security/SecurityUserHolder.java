package com.example.springboot.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUserHolder {

    public static AuthenticationUser getCurrentUser() {
        return (AuthenticationUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }
}
