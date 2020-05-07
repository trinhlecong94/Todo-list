package com.learning.springboot.error;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(Long id) {
        super("Task id " + id + " is not found.");
    }

}
