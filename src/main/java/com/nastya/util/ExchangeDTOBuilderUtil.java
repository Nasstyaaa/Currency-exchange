package com.nastya.util;

import com.nastya.dto.ExchangeDTO;
import com.nastya.model.ExchangeRate;

import java.math.BigDecimal;

public class ExchangeDTOBuilderUtil {
    public static ExchangeDTO create(ExchangeRate exchangeRate, double amount, BigDecimal targetRate){
        double convertedAmount = targetRate * amount;
        return new ExchangeDTO(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), targetRate,
                amount, convertedAmount);
    }
}
