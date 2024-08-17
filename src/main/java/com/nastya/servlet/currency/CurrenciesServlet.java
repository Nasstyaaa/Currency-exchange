package com.nastya.servlet.currency;


import com.nastya.dao.CurrencyDAO;
import com.nastya.exception.AppException;
import com.nastya.exception.IncorrectDataRequestException;
import com.nastya.exception.MissingFormFieldException;
import com.nastya.model.Currency;
import com.nastya.util.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            List<Currency> currencies = currencyDAO.findAll();
            ResponseUtil.send(response, HttpServletResponse.SC_OK, currencies);
        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String code = request.getParameter("code");
            String fullName = request.getParameter("name");
            String sign = request.getParameter("sign");

            if (code.trim().isEmpty() || fullName.trim().isEmpty() || sign.trim().isEmpty()) {
                throw new MissingFormFieldException();
            } else if (code.length() != 3) {
                throw new IncorrectDataRequestException();
            } else if (!code.matches("^[A-Za-z]+$") || !fullName.matches("^[A-Za-z\\s]+$")) {
                throw new IncorrectDataRequestException();
            }

            Currency createdCurrency = currencyDAO.save(new Currency(null, code, fullName, sign));
            ResponseUtil.send(response, HttpServletResponse.SC_CREATED, createdCurrency);
        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception);
        }
    }
}