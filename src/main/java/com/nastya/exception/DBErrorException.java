package com.nastya.exception;


import jakarta.servlet.http.HttpServletResponse;

public class DBErrorException extends AppException{
    public DBErrorException(){
        super("The database is unavailable", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
