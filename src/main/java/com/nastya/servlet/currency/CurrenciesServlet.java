package com.nastya.servlet.currency;


import com.google.gson.Gson;
import com.nastya.dao.CurrencyDAO;
import com.nastya.exception.CurrencyCodeExistsException;
import com.nastya.exception.DBErrorException;
import com.nastya.model.Currency;
import com.nastya.util.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            ResponseUtil.send(response, HttpServletResponse.SC_OK, currencyDAO.findAll());
        } catch (DBErrorException exception) {
            ResponseUtil.sendException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String code = request.getParameter("code");
            String fullName = request.getParameter("full_name");
            String sign = request.getParameter("sign");

            if (code == null || code.length() > 3 || fullName == null || sign == null) {
                ResponseUtil.sendException(response, HttpServletResponse.SC_BAD_REQUEST,
                        "The required form field is missing");
                return;
            }

            Currency createdCurrency = currencyDAO.save(new Currency(0, code, fullName, sign));
            ResponseUtil.send(response, HttpServletResponse.SC_CREATED, createdCurrency);

        } catch (DBErrorException exception) {
            ResponseUtil.sendException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
        } catch (CurrencyCodeExistsException exception) {
            ResponseUtil.sendException(response, HttpServletResponse.SC_CONFLICT, exception.getMessage());
        }
    }
}