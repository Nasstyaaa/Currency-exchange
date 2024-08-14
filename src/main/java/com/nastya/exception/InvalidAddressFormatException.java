package com.nastya.exception;

import jakarta.servlet.http.HttpServletResponse;

public class InvalidAddressFormatException extends AppException{
    public InvalidAddressFormatException(){
        super("The currency code is missing from the address", HttpServletResponse.SC_BAD_REQUEST);
    }
}
