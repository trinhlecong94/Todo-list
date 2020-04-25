package com.learning.springboot.error;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(Long id) {
        super("Book id not found : " + id);
    }

}
