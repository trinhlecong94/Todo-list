package com.learning.springboot.error;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(Long id) {
        super("Todo id not found : " + id);
    }

}
