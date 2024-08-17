package com.nastya.exception;

import jakarta.servlet.http.HttpServletResponse;

public class IncorrectDataRequestException extends AppException{

    public IncorrectDataRequestException(){
        super("Incorrect data has been entered", HttpServletResponse.SC_BAD_REQUEST);
    }
}
