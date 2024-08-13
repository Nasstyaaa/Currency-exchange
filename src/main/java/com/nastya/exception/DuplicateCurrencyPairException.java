package com.nastya.exception;

public class DuplicateCurrencyPairException extends RuntimeException{
    public DuplicateCurrencyPairException(){
        super("A currency pair with this code already exists");
    }
}
