package com.nastya.servlet.exchangerate;

import com.nastya.dao.ExchangeRatesDAO;
import com.nastya.exception.AppException;
import com.nastya.exception.InvalidAddressFormatException;
import com.nastya.exception.MissingFormFieldException;
import com.nastya.model.ExchangeRate;
import com.nastya.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();

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
            }else {
                throw new InvalidAddressFormatException();
            }

            ResponseUtil.send(response, HttpServletResponse.SC_OK, exchangeRatesDAO.find(baseCode, targetCode));
        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception.getStatus(), exception.getMessage());
        }
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
        }else {
            this.doPatch(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String baseCode;
        String targetCode;
        try {
            String pathInfo = request.getPathInfo();
            String rate = request.getParameter("rate");

            if (rate == null) {
                throw new MissingFormFieldException();
            } else if (pathInfo.length() == 7) {
                baseCode = pathInfo.substring(1, 4);
                targetCode = pathInfo.substring(4, 7);
            }else {
                throw new InvalidAddressFormatException();
            }

            ExchangeRate exchangeRate = exchangeRatesDAO.find(baseCode, targetCode);
            ResponseUtil.send(response, HttpServletResponse.SC_OK,
                    exchangeRatesDAO.update(exchangeRate, BigDecimal.valueOf(Double.valueOf(rate))));
        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception.getStatus(), exception.getMessage());
        }
    }
}
