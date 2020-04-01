package com.jkopay.industry.transport.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public Map<String, Object> demo(){
        return ImmutableMap.of("id", 10012L, "name", "John");
    }
}
