package com.b77.leetcodeapi.leetcodeapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/helloworld")
public class HelloWorldController {

    @GetMapping()
    public String helloWorld(){
        return "HelloWorld!";
    }

}
