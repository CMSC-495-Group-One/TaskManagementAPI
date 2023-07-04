package com.group1.taskmanagement.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.base}/users")
public class UserController {

    @GetMapping("/private")
    public String hello() {
        return "Oops private";
    }
}
