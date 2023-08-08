package org.macbeth.ecommarketapp.service;

import org.macbeth.ecommarketapp.limiter.RateLimit;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {

    @RateLimit
    public void exampleMethod() {
        System.out.println("put something to DB");
    }
}
