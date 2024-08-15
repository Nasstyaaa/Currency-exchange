package com.nastya.builder;

import com.nastya.dto.ExchangeDTO;
import com.nastya.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeDTOBuilder {
    public static ExchangeDTO create(ExchangeRate exchangeRate, BigDecimal amount, BigDecimal targetRate){
        BigDecimal convertedAmount =  targetRate.multiply(amount).setScale(3, RoundingMode.HALF_UP);
        return new ExchangeDTO(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), targetRate,
                amount, convertedAmount);
    }
}
