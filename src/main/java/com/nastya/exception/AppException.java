package com.nastya.exception;

public abstract class AppException extends RuntimeException{
    private int status;

    public AppException(String message, int status){
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
