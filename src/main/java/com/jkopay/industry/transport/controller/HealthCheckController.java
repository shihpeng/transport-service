package com.jkopay.industry.transport.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health_check")
    public String healthCheck(){
        return "success";
    }
}
