package com.nastya.dto;

import com.nastya.model.Currency;
import com.nastya.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeDTO {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public ExchangeDTO(ExchangeRate exchangeRate, BigDecimal amount, BigDecimal targetRate){
        BigDecimal convertedAmount =  targetRate.multiply(amount).setScale(2, RoundingMode.HALF_UP);

        this.baseCurrency = exchangeRate.getBaseCurrency();
        this.targetCurrency = exchangeRate.getTargetCurrency();
        this.rate = targetRate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }
}
