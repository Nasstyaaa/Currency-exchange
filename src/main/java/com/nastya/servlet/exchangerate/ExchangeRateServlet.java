package com.nastya.servlet.exchangerate;

import com.nastya.dao.ExchangeRatesDAO;
import com.nastya.exception.DBErrorException;
import com.nastya.exception.ExchangeRateNotFoundException;
import com.nastya.exception.InvalidAddressFormatException;
import com.nastya.model.ExchangeRate;
import com.nastya.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo.length() == 1) {
                throw new InvalidAddressFormatException();
            }
            String baseCode = pathInfo.substring(1, 4);
            String targetCode = pathInfo.substring(4, 7);

            ResponseUtil.send(response, HttpServletResponse.SC_OK, exchangeRatesDAO.find(baseCode, targetCode));

        } catch (DBErrorException exception) {
            ResponseUtil.sendException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
        } catch (InvalidAddressFormatException exception) {
            ResponseUtil.sendException(response, HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        } catch (ExchangeRateNotFoundException exception) {
            ResponseUtil.sendException(response, HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
        }
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo.length() == 1 || pathInfo.length() > 7) {
                throw new InvalidAddressFormatException();
            }
            String baseCode = pathInfo.substring(1, 4);
            String targetCode = pathInfo.substring(4, 7);
            Double rate = Double.valueOf(request.getParameter("rate"));

            ExchangeRate exchangeRate = exchangeRatesDAO.find(baseCode, targetCode);
            ResponseUtil.send(response, HttpServletResponse.SC_OK, exchangeRatesDAO.update(exchangeRate, rate));

        } catch (DBErrorException exception) {
            ResponseUtil.sendException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
        } catch (InvalidAddressFormatException exception) {
            ResponseUtil.sendException(response, HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        } catch (ExchangeRateNotFoundException exception) {
            ResponseUtil.sendException(response, HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
        }
    }
}
