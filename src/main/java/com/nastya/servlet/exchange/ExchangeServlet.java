package com.nastya.servlet.exchange;

import com.nastya.exception.AppException;
import com.nastya.exception.InvalidAddressFormatException;
import com.nastya.exception.MissingFormFieldException;
import com.nastya.service.ExchangeService;
import com.nastya.util.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String baseCode = request.getParameter("from");
            String targetCode = request.getParameter("to");
            String amount = request.getParameter("amount");

            if (baseCode == null || targetCode == null || amount == null){
                throw new MissingFormFieldException();
            }else if (Double.valueOf(amount) <= 0){
                ResponseUtil.sendException(response, HttpServletResponse.SC_BAD_REQUEST,
                        "The amount cannot be zero or negative");
                return;
            }

            ResponseUtil.send(response, HttpServletResponse.SC_OK, new ExchangeService()
                    .convertAmount(baseCode, targetCode, BigDecimal.valueOf(Double.valueOf(amount))));

        }catch (AppException exception){
            ResponseUtil.sendException(response, exception.getStatus(), exception.getMessage());
        }
    }
}

