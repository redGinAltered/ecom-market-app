package org.macbeth.ecommarketapp.controller;

import org.macbeth.ecommarketapp.limiter.RateLimit;
import org.macbeth.ecommarketapp.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    private ExampleService exampleService;

    @GetMapping("/")
    @ResponseBody
    @RateLimit
    public ResponseEntity ping() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/example")
    @ResponseBody
    public ResponseEntity callExample() {
        exampleService.exampleMethod();
        return new ResponseEntity(HttpStatus.OK);
    }
}
