package com.example.springboot.exception;

public class ServiceException extends RuntimeException {
    public String getCode() {
        return code;
    }

    private String code;

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String code, String message) {
        super(message);
        this.code = code;

    }
}
