package com.nastya.service;

import com.nastya.dao.ExchangeRateDAO;
import com.nastya.dto.ExchangeDTO;
import com.nastya.dto.ExchangeRequestDTO;
import com.nastya.exception.ExchangeRateNotFoundException;
import com.nastya.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeService {
    private final ExchangeRateDAO exchangeRatesDAO = new ExchangeRateDAO();

    public ExchangeDTO convertAmount(ExchangeRequestDTO exchangeRequestDTO){
        BigDecimal amount = exchangeRequestDTO.getAmount();
        String baseCode = exchangeRequestDTO.getBaseCode();
        String targetCode = exchangeRequestDTO.getTargetCode();

        try {
            ExchangeRate exchangeRate = exchangeRatesDAO.find(baseCode, targetCode);
            BigDecimal convertedAmount =  exchangeRate.getRate().multiply(amount).setScale(2, RoundingMode.HALF_UP);

            return new ExchangeDTO(exchangeRate, amount, exchangeRate.getRate(), convertedAmount);
        }catch (ExchangeRateNotFoundException ignored){
        }


        try {
            ExchangeRate exchangeRate = exchangeRatesDAO.find(targetCode, baseCode);
            BigDecimal targetRate = BigDecimal.ONE.divide(exchangeRate.getRate(), 2, RoundingMode.HALF_UP);
            BigDecimal convertedAmount =  targetRate.multiply(amount).setScale(2, RoundingMode.HALF_UP);

            return new ExchangeDTO(exchangeRate, amount, targetRate, convertedAmount);
        }catch (ExchangeRateNotFoundException ignored){
        }


        try {
            ExchangeRate exchangeRateBase = exchangeRatesDAO.find("USD", baseCode);
            ExchangeRate exchangeRateTarget = exchangeRatesDAO.find("USD", targetCode);
            ExchangeRate exchangeRate = new ExchangeRate(0, exchangeRateBase.getTargetCurrency(),
                    exchangeRateTarget.getTargetCurrency(),null);

            BigDecimal targetRate = exchangeRateBase.getRate().divide(exchangeRateTarget.getRate(), 2,
                    RoundingMode.HALF_UP);
            BigDecimal convertedAmount =  targetRate.multiply(amount).setScale(2, RoundingMode.HALF_UP);

            return new ExchangeDTO(exchangeRate, amount, targetRate, convertedAmount);
        }catch (ExchangeRateNotFoundException exchangeException){
            throw new ExchangeRateNotFoundException();
        }
    }
}
