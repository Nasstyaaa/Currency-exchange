package com.nastya.exception;

import jakarta.servlet.http.HttpServletResponse;

public class DuplicateCurrencyPairException extends AppException{
    public DuplicateCurrencyPairException(){
        super("A currency pair with this code already exists", HttpServletResponse.SC_CONFLICT);
    }
}
