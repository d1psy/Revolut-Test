package com.revolut.error;

public class ErrorResponse extends RuntimeException {

    private int errorCode;

    public ErrorResponse(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
