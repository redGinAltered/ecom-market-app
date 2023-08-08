package org.macbeth.ecommarketapp.limiter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RateLimitAdvice {

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity handleException(RateLimitException e) {
        return new ResponseEntity(HttpStatus.BAD_GATEWAY);
    }
}
