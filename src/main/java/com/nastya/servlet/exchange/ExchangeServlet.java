package com.nastya.servlet.exchange;

import com.nastya.dto.ExchangeDTO;
import com.nastya.dto.ExchangeRequestDTO;
import com.nastya.exception.AppException;
import com.nastya.exception.IncorrectDataRequestException;
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

    private final ExchangeService exchangeService = new ExchangeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String baseCode = request.getParameter("from");
            String targetCode = request.getParameter("to");
            String amount = request.getParameter("amount");

            if ((baseCode.trim().isEmpty() || targetCode.trim().isEmpty() || amount.trim().isEmpty())) {
                throw new MissingFormFieldException();
            } else if (Double.valueOf(amount) <= 0) {
                throw new IncorrectDataRequestException();
            }

            ExchangeDTO exchangeDTO = exchangeService.convertAmount(
                    new ExchangeRequestDTO(baseCode, targetCode, new BigDecimal(amount)));
            ResponseUtil.send(response, HttpServletResponse.SC_OK, exchangeDTO);

        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception);
        } catch (NumberFormatException e) {
            ResponseUtil.sendException(response, new IncorrectDataRequestException());
        }
    }
}

