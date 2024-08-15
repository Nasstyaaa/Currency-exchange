package com.nastya.servlet.currency;


import com.nastya.dao.CurrencyDAO;
import com.nastya.exception.AppException;
import com.nastya.exception.MissingFormFieldException;
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
        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception.getStatus(), exception.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String code = request.getParameter("code");
            String fullName = request.getParameter("name");
            String sign = request.getParameter("sign");

            if (code == null || fullName == null || sign == null) {
                throw new MissingFormFieldException();
            } else if (code.length() > 3) {
                ResponseUtil.sendException(response, HttpServletResponse.SC_BAD_REQUEST,
                        "The code field must consist of 3 letters");
            }

            Currency createdCurrency = currencyDAO.save(new Currency(0, code, fullName, sign));
            ResponseUtil.send(response, HttpServletResponse.SC_CREATED, createdCurrency);
        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception.getStatus(), exception.getMessage());
        }
    }
}