package com.will.exceptions;

public class EntityAlreadyRegisteredException extends RuntimeException {
    public EntityAlreadyRegisteredException(String message) {
        super(message);
    }
}