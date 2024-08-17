package com.nastya.servlet.exchangerate;

import com.nastya.dao.ExchangeRateDAO;
import com.nastya.exception.AppException;
import com.nastya.exception.IncorrectDataRequestException;
import com.nastya.exception.InvalidAddressFormatException;
import com.nastya.exception.MissingFormFieldException;
import com.nastya.model.ExchangeRate;
import com.nastya.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateDAO exchangeRatesDAO = new ExchangeRateDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String baseCode;
        String targetCode;
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo.length() == 7) {
                baseCode = pathInfo.substring(1, 4);
                targetCode = pathInfo.substring(4, 7);
            } else {
                throw new InvalidAddressFormatException();
            }

            ExchangeRate exchangeRate = exchangeRatesDAO.find(baseCode, targetCode);
            ResponseUtil.send(response, HttpServletResponse.SC_OK, exchangeRate);
        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception);
        }
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
        } else {
            this.doPatch(req, resp);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String baseCode;
        String targetCode;
        try {
            String pathInfo = request.getPathInfo();

            StringBuilder body = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
            }
            String[] requestBody = body.toString().split("=");
            String rate = requestBody[0];
            String rateValue = requestBody[1];

            if (!Objects.equals(rate, "rate")) {
                throw new MissingFormFieldException();
            } else if (Double.parseDouble(rateValue) <= 0 ||rateValue.trim().isEmpty()) {
                throw new IncorrectDataRequestException();
            } else if (pathInfo.length() != 7) {
                throw new InvalidAddressFormatException();
            }

            baseCode = pathInfo.substring(1, 4);
            targetCode = pathInfo.substring(4, 7);

            ExchangeRate foundExchangeRate = exchangeRatesDAO.find(baseCode, targetCode);
            ExchangeRate exchangeRate = exchangeRatesDAO.update(foundExchangeRate, new BigDecimal(rateValue));
            ResponseUtil.send(response, HttpServletResponse.SC_OK, exchangeRate);
        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception);
        }
    }
}
