package com.example.helloworldgcp.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1")
public class HelloWorldController {

    @Value("${application.custom.message}")
    private String message;

    @Value("${application.api-key}")
    private String apiKey;

    @GetMapping(value = "helloWorld")
    public ResponseEntity<String> helloWorld(){
        System.out.println("GET Hello World called !!!, Key = " + apiKey);
        return ResponseEntity.ok(message + " and key = " + apiKey);
    }

}
