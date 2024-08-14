package com.nastya.service;

import com.nastya.dao.ExchangeRatesDAO;
import com.nastya.dto.ExchangeDTO;
import com.nastya.exception.ExchangeRateNotFoundException;
import com.nastya.model.ExchangeRate;
import com.nastya.util.ExchangeDTOBuilderUtil;

import java.math.BigDecimal;

public class ExchangeService {
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();
    private ExchangeRate exchangeRate;

    public ExchangeDTO convertAmount(String baseCode, String targetCode, double amount){
        try {
            exchangeRate = exchangeRatesDAO.find(baseCode, targetCode);

            return ExchangeDTOBuilderUtil.create(exchangeRate, amount, exchangeRate.getRate());
        }catch (ExchangeRateNotFoundException ignored){
        }


        try {
            exchangeRate = exchangeRatesDAO.find(targetCode, baseCode);
            BigDecimal targetRate = BigDecimal.valueOf((long) (1. / exchangeRate.getRate()), 6);

            return ExchangeDTOBuilderUtil.create(exchangeRate, amount, Double.parseDouble(targetRate));
        }catch (ExchangeRateNotFoundException ignored){
        }


        try {
            ExchangeRate exchangeRateBase = exchangeRatesDAO.find("USD", baseCode);
            ExchangeRate exchangeRateTarget = exchangeRatesDAO.find("USD", targetCode);
            System.out.println(exchangeRateTarget.getRate() + " " + exchangeRateBase.getRate());
            String targetRate = String.format("%.6f",exchangeRateBase.getRate() % exchangeRateTarget.getRate());
            System.out.println(targetRate);
            return ExchangeDTOBuilderUtil.create(exchangeRate, amount, Double.parseDouble(targetRate));
        }catch (ExchangeRateNotFoundException exchangeException){
            throw new ExchangeRateNotFoundException();
        }
    }
}
