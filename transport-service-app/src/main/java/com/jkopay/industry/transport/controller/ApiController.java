package com.jkopay.industry.transport.controller;

import com.jkopay.industry.transport.entity.User;
import com.jkopay.industry.transport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private UserService userService;

    @Autowired
    public ApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public User getUser(@RequestParam("id") Long id){
        return userService.getUser(id);
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }
}
