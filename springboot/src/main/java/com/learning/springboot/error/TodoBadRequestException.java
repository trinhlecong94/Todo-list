package com.learning.springboot.error;

public class TodoBadRequestException extends RuntimeException {

    public TodoBadRequestException(String fieldName) {
        super("Required field " + fieldName + " must not be blank");
    }

}
