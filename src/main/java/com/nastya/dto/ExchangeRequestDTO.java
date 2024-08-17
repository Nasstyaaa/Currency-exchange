package com.nastya.dto;

import java.math.BigDecimal;

public class ExchangeRequestDTO {
    private String baseCode;
    private String targetCode;
    private BigDecimal amount;

    public ExchangeRequestDTO(String baseCode, String targetCode, BigDecimal amount) {
        this.baseCode = baseCode;
        this.targetCode = targetCode;
        this.amount = amount;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
