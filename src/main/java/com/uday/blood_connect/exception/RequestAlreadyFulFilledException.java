package com.uday.blood_connect.exception;

public class RequestAlreadyFulFilledException extends RuntimeException {
    public RequestAlreadyFulFilledException(String message) {
        super(message);
    }
}
