package com.nastya.dto;

import com.nastya.model.Currency;

import java.math.BigDecimal;

public class ExchangeDTO {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public ExchangeDTO(Currency baseCurrency, Currency targetCurrency, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }
}
