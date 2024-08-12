package com.nastya.exception;

public class CurrencyNotFoundException extends RuntimeException{
    public CurrencyNotFoundException(){
        super("The currency was not found");
    }
}
