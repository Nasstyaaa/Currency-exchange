package com.nastya.exception;

public class InvalidAddressFormatException extends RuntimeException{
    public InvalidAddressFormatException(){
        super("The currency code is missing from the address");
    }
}
