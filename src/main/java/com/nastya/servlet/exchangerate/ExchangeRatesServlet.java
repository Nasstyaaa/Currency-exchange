package com.nastya.servlet.exchangerate;

import com.nastya.dao.ExchangeRatesDAO;
import com.nastya.exception.*;
import com.nastya.util.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            ResponseUtil.send(response, HttpServletResponse.SC_OK, exchangeRatesDAO.findAll());
        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception.getStatus(), exception.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String baseCode = request.getParameter("baseCurrencyCode");
            String targetCode = request.getParameter("targetCurrencyCode");
            String rate = request.getParameter("rate");

            if (baseCode == null || targetCode == null || rate == null) {
                throw new MissingFormFieldException();
            }else if(Double.valueOf(rate) <=0 || baseCode.equals(targetCode)){
                ResponseUtil.sendException(response, HttpServletResponse.SC_BAD_REQUEST,
                        "The form fields are filled in incorrectly");
                return;
            }

            ResponseUtil.send(response, HttpServletResponse.SC_CREATED,
                    exchangeRatesDAO.save(baseCode, targetCode, BigDecimal.valueOf(Double.valueOf(rate))));
        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception.getStatus(), exception.getMessage());
        }
    }
}
