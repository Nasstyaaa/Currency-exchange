package com.nastya.exception;

import jakarta.servlet.http.HttpServletResponse;

public class ExchangeRateNotFoundException extends AppException{
    public ExchangeRateNotFoundException(){
        super("The exchange rate for the pair was not found", HttpServletResponse.SC_NOT_FOUND);
    }
}
