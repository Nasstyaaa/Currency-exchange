package com.nastya.exception;

public class CurrencyCodeExistsException extends RuntimeException{
    public CurrencyCodeExistsException(){
        super("A currency with this code already exists");
    }
}
