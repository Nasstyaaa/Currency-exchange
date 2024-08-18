package com.nastya.servlet.currency;

import com.nastya.dao.CurrencyDAO;
import com.nastya.exception.AppException;
import com.nastya.exception.InvalidAddressFormatException;
import com.nastya.model.Currency;
import com.nastya.util.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String code;
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo.length() != 4) {
                throw new InvalidAddressFormatException();
            }
            code = pathInfo.substring(1);

            Currency currency = currencyDAO.find(code);
            ResponseUtil.send(response, HttpServletResponse.SC_OK, currency);
        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception);
        }
    }
}
