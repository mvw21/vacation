package com.example.sportshop.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User already exists")
public class UserAlreadyExistException extends RuntimeException {

    private int status;

    public UserAlreadyExistException() {
        this.status = 409;
    }

    public UserAlreadyExistException(String message) {
        super(message);
        this.status = 409;
    }

    public int getStatusCode() {
        return status;
    }
}
