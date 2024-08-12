package com.nastya.servlet.currency;

import com.google.gson.Gson;
import com.nastya.dao.CurrencyDAO;
import com.nastya.exception.CurrencyNotFoundException;
import com.nastya.exception.DBErrorException;
import com.nastya.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import com.nastya.model.Currency;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo.length() == 1) {
            ResponseUtil.sendException(response, HttpServletResponse.SC_BAD_REQUEST,
                    "The currency code is missing from the address");
            return;
        }

        String code = pathInfo.substring(1);

        try {
            Currency currency = currencyDAO.find(code);
            ResponseUtil.send(response, HttpServletResponse.SC_OK, currency);

        } catch (DBErrorException exception) {
            ResponseUtil.sendException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
        } catch (CurrencyNotFoundException exception) {
            ResponseUtil.sendException(response, HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
        }
    }
}
