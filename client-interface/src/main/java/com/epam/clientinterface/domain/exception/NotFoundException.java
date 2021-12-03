package com.epam.clientinterface.domain.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Class<?> clazz, Long id) {
        super(String.format("Entity %s with id %d not found", clazz.getSimpleName(), id));
    }
}
