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
        try {
            ExchangeRate exchangeRate = exchangeRatesDAO.find(exchangeRequestDTO.getBaseCode(),
                    exchangeRequestDTO.getTargetCode());

            return new ExchangeDTO(exchangeRate, exchangeRequestDTO.getAmount(), exchangeRate.getRate());
        }catch (ExchangeRateNotFoundException ignored){
        }


        try {
            ExchangeRate exchangeRate = exchangeRatesDAO.find(exchangeRequestDTO.getTargetCode(),
                    exchangeRequestDTO.getBaseCode());
            BigDecimal targetRate = BigDecimal.ONE.divide(exchangeRate.getRate(), 2, RoundingMode.HALF_UP);

            return new ExchangeDTO(exchangeRate, exchangeRequestDTO.getAmount(), targetRate);
        }catch (ExchangeRateNotFoundException ignored){
        }


        try {
            ExchangeRate exchangeRateBase = exchangeRatesDAO.find("USD", exchangeRequestDTO.getBaseCode());
            ExchangeRate exchangeRateTarget = exchangeRatesDAO.find("USD", exchangeRequestDTO.getTargetCode());
            ExchangeRate exchangeRate = new ExchangeRate(0, exchangeRateBase.getTargetCurrency(),
                    exchangeRateTarget.getTargetCurrency(),null);

            BigDecimal targetRate = exchangeRateBase.getRate().divide(exchangeRateTarget.getRate(), 2,
                    RoundingMode.HALF_UP);

            return new ExchangeDTO(exchangeRate, exchangeRequestDTO.getAmount(), targetRate);
        }catch (ExchangeRateNotFoundException exchangeException){
            throw new ExchangeRateNotFoundException();
        }
    }
}
