package com.nastya.exception;

public class InvalidCurrencyPairException extends RuntimeException{
    public InvalidCurrencyPairException(){
        super("One (or both) currency from the currency pair does not exist in the database");
    }
}
