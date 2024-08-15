package com.nastya.service;

import com.nastya.dao.ExchangeRatesDAO;
import com.nastya.dto.ExchangeDTO;
import com.nastya.exception.ExchangeRateNotFoundException;
import com.nastya.model.ExchangeRate;
import com.nastya.util.ExchangeDTOBuilderUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeService {
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();
    private ExchangeRate exchangeRate;

    public ExchangeDTO convertAmount(String baseCode, String targetCode, BigDecimal amount){
        try {
            exchangeRate = exchangeRatesDAO.find(baseCode, targetCode);

            return ExchangeDTOBuilderUtil.create(exchangeRate, amount, exchangeRate.getRate());
        }catch (ExchangeRateNotFoundException ignored){
        }


        try {
            exchangeRate = exchangeRatesDAO.find(targetCode, baseCode);
            BigDecimal targetRate = BigDecimal.ONE.divide(exchangeRate.getRate(), 3, RoundingMode.HALF_UP);

            return ExchangeDTOBuilderUtil.create(exchangeRate, amount, targetRate);
        }catch (ExchangeRateNotFoundException ignored){
        }


        try {
            ExchangeRate exchangeRateBase = exchangeRatesDAO.find("USD", baseCode);
            ExchangeRate exchangeRateTarget = exchangeRatesDAO.find("USD", targetCode);
            exchangeRate = new ExchangeRate(0, exchangeRateBase.getTargetCurrency(),
                    exchangeRateTarget.getTargetCurrency(),null);

            BigDecimal targetRate = exchangeRateBase.getRate().divide(exchangeRateTarget.getRate(), 3,
                    RoundingMode.HALF_UP);

            return ExchangeDTOBuilderUtil.create(exchangeRate, amount, targetRate);
        }catch (ExchangeRateNotFoundException exchangeException){
            throw new ExchangeRateNotFoundException();
        }
    }
}
