package com.epam.clientinterface.exception;

public class PlanNotEnumException extends RuntimeException {
    public PlanNotEnumException(String plan) {
        super(String.format("Plan of plan=%s is not enum", plan));
    }
}
