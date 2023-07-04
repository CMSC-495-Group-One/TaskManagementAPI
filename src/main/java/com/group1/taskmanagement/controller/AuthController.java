package com.group1.taskmanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.base}/auth")
public class AuthController {

    @GetMapping("/hello")
    public String hello() {
        return "AuthController is working";
    }
}
