package com.quetzalcoatl.restaurants.util;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}