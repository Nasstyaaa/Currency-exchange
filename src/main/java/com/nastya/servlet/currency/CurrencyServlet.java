package com.nastya.servlet.currency;

import com.nastya.dao.CurrencyDAO;
import com.nastya.exception.AppException;
import com.nastya.exception.InvalidAddressFormatException;
import com.nastya.util.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo.length() == 1) {
                throw new InvalidAddressFormatException();
            }
            String code = pathInfo.substring(1);

            ResponseUtil.send(response, HttpServletResponse.SC_OK, new CurrencyDAO().find(code));
        } catch (AppException exception) {
            ResponseUtil.sendException(response, exception.getStatus(), exception.getMessage());
        }
    }
}
