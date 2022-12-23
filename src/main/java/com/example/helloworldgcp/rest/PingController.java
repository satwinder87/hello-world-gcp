package com.example.helloworldgcp.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping
    public String ping(){
        return "Pong";
    }
}
