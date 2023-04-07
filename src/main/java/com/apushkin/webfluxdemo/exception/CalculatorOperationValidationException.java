package com.apushkin.webfluxdemo.exception;

public class CalculatorOperationValidationException extends RuntimeException {

    private static final String MSG = "Allowed operations are: +, -, *, /";
    private static final int errorCode = 101;
    private final String operation;

    public CalculatorOperationValidationException(String input) {
        super(MSG);
        this.operation = input;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getOperation() {
        return operation;
    }
}
