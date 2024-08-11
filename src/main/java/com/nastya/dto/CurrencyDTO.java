package com.nastya.dto;

public class CurrencyDTO {
    private String code;
    private String fullName;
    private String sign;

    public CurrencyDTO(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public String getCode() {
        return code;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSign() {
        return sign;
    }
}
