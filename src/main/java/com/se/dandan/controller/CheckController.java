package com.se.dandan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/check")
public class CheckController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
