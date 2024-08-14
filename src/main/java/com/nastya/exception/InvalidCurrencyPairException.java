package com.nastya.exception;

import jakarta.servlet.http.HttpServletResponse;

public class InvalidCurrencyPairException extends AppException{
    public InvalidCurrencyPairException(){
        super("One (or both) currency from the currency pair does not exist in the database",
                HttpServletResponse.SC_NOT_FOUND);
    }
}
