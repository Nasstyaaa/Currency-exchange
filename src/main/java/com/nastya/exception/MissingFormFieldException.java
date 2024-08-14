package com.nastya.exception;

import jakarta.servlet.http.HttpServletResponse;

public class MissingFormFieldException extends AppException{
    public MissingFormFieldException(){
        super("The required form field is missing", HttpServletResponse.SC_BAD_REQUEST);
    }
}
