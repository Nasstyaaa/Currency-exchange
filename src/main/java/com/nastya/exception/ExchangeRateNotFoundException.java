package com.nastya.exception;

public class ExchangeRateNotFoundException extends RuntimeException{
    public ExchangeRateNotFoundException(){
        super("The exchange rate for the pair was not found");
    }
}
