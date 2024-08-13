package com.nastya.exception;

public class MissingFormFieldException extends RuntimeException{
    public MissingFormFieldException(){
        super("The required form field is missing");
    }
}
