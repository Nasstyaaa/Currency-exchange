package com.nastya.exception;


public class DBErrorException extends RuntimeException{
    public DBErrorException(){
        super("The database is unavailable");
    }
}
