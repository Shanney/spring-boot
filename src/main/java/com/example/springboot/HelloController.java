package com.example.springboot;

import com.example.springboot.security.AuthenticationUser;
import com.example.springboot.security.SecurityUserHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/greetings")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/qianbaoCreate")
    public String test(){
        AuthenticationUser user = SecurityUserHolder.getCurrentUser();
        return user.getUsername() + ": successfully entering qianbaoCreate!";
    }

    @GetMapping("/api/testRest")
    public String testRest(){
        AuthenticationUser user = SecurityUserHolder.getCurrentUser();
        return user.getUsername() + ": successfully entering api/testRest!";
    }
}
