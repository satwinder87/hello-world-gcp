package com.example.helloworldgcp.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1")
public class HelloWorldController {

    @GetMapping(value = "helloWorld")
    public ResponseEntity<String> helloWorld(){
        System.out.println("GET Hello World called !!!");
        return ResponseEntity.ok("Hello World !");
    }

}
