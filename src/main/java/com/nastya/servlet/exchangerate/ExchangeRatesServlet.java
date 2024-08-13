package com.nastya.servlet.exchangerate;

import com.nastya.dao.ExchangeRatesDAO;
import com.nastya.exception.DBErrorException;
import com.nastya.exception.DuplicateCurrencyPairException;
import com.nastya.exception.InvalidCurrencyPairException;
import com.nastya.exception.MissingFormFieldException;
import com.nastya.util.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            ResponseUtil.send(response, HttpServletResponse.SC_OK, exchangeRatesDAO.findAll());
        } catch (DBErrorException exception) {
            ResponseUtil.sendException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            String baseCode = request.getParameter("baseCurrencyCode");
            String targetCode = request.getParameter("targetCurrencyCode");
            double rate = Double.parseDouble(request.getParameter("rate"));

            if (baseCode == null || targetCode == null || rate == 0 || baseCode.equals(targetCode)){
                throw new MissingFormFieldException();
            }

            ResponseUtil.send(response, HttpServletResponse.SC_CREATED, exchangeRatesDAO.save(baseCode, targetCode, rate));
        }catch (DBErrorException exception){
            ResponseUtil.sendException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
        }catch (InvalidCurrencyPairException exception){
            ResponseUtil.sendException(response, HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
        }catch (MissingFormFieldException exception){
            ResponseUtil.sendException(response, HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        }catch (DuplicateCurrencyPairException exception){
            ResponseUtil.sendException(response, HttpServletResponse.SC_CONFLICT, exception.getMessage());
        }
    }
}
