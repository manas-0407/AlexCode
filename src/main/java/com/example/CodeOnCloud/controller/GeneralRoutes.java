package com.example.CodeOnCloud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralRoutes {

    @GetMapping("/get")
    public ResponseEntity<String> get(){
        return ResponseEntity.ok("Hello Manas");
    }


}
