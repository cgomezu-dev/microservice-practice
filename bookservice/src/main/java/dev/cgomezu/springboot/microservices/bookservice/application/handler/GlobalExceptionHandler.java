package dev.cgomezu.springboot.microservices.bookservice.application.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.cgomezu.springboot.microservices.bookservice.domain.model.exception.BookNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleBookNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(e.getMessage());
    }
}
