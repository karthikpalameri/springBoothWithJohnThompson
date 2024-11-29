package guru.springframework.spring6restmvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Exception Handling Technique 3 - Exception Handler on global level using ResponseStatus
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Value Not Found")
public class NotFoundException extends RuntimeException{
    public NotFoundException() {
        System.out.println("NotFoundException constructor called");
    }

    public NotFoundException(String message) {
        super(message);
        System.out.println("NotFoundException constructor called with message");
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
        System.out.println("NotFoundException constructor called with message and cause");
    }

    public NotFoundException(Throwable cause) {
        super(cause);
        System.out.println("NotFoundException constructor called with cause");
    }

    public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        System.out.println("NotFoundException constructor called with message, cause, enableSuppression and writableStackTrace");
    }
}
