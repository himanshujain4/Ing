package com.ing.assessment.mortgage.exception;

public class CsvLoadException extends RuntimeException {
    public CsvLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}