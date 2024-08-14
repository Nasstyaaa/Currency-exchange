package com.nastya.exception;

import jakarta.servlet.http.HttpServletResponse;

public class CurrencyNotFoundException extends AppException{
    public CurrencyNotFoundException(){
        super("The currency was not found", HttpServletResponse.SC_NOT_FOUND);
    }
}
