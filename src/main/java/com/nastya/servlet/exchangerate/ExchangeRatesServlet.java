package com.nastya.servlet.exchangerate;

import com.nastya.dao.CurrencyDAO;
import com.nastya.dao.ExchangeRateDAO;
import com.nastya.exception.*;
import com.nastya.model.Currency;
import com.nastya.model.ExchangeRate;
import com.nastya.util.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRateDAO exchangeRatesDAO = new ExchangeRateDAO();
    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            List<ExchangeRate> rates = exchangeRatesDAO.findAll();
            ResponseUtil.send(response, HttpServletResponse.SC_OK, rates);
        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String baseCode = request.getParameter("baseCurrencyCode");
            String targetCode = request.getParameter("targetCurrencyCode");
            String rate = request.getParameter("rate");

            if (rate.trim().isEmpty() || baseCode.equals(targetCode)) {
                throw new MissingFormFieldException();
            }else if (Double.valueOf(rate) <= 0) {
                throw new IncorrectDataRequestException();
            }

            Currency baseCurrency = currencyDAO.find(baseCode);
            Currency targetCurrency = currencyDAO.find(targetCode);
            ExchangeRate exchangeRate =
                    exchangeRatesDAO.save(baseCurrency, targetCurrency, new BigDecimal(rate));
            ResponseUtil.send(response, HttpServletResponse.SC_CREATED, exchangeRate);
        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception);
        } catch (NumberFormatException e){
            ResponseUtil.sendException(response, new IncorrectDataRequestException());
        }
    }
}
