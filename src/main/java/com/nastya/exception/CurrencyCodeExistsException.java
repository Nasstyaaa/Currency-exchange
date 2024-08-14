package com.nastya.exception;

import jakarta.servlet.http.HttpServletResponse;

public class CurrencyCodeExistsException extends AppException{
    public CurrencyCodeExistsException(){
        super("A currency with this code already exists", HttpServletResponse.SC_CONFLICT);
    }
}
