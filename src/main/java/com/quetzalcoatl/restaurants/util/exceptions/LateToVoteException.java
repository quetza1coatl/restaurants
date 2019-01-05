package com.quetzalcoatl.restaurants.util.exceptions;

public class LateToVoteException extends RuntimeException {
    public LateToVoteException(String message) {
        super(message);
    }
}
