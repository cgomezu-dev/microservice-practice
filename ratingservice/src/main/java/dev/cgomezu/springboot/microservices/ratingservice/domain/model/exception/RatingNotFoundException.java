package dev.cgomezu.springboot.microservices.ratingservice.domain.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RatingNotFoundException extends RuntimeException {

    public RatingNotFoundException() {
        super();
    }

    public RatingNotFoundException(String message) {
        super(message);
    }

    public RatingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

   public RatingNotFoundException(Throwable cause) {
        super(cause);
    } 
}
