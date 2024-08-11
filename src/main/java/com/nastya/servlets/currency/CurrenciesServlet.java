package com.nastya.servlets.currency;


import com.google.gson.Gson;
import com.nastya.dao.CurrencyDAO;
import com.nastya.dto.CurrencyDTO;
import com.nastya.models.Сurrency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private Gson gson;
    CurrencyDAO currencyDAO;

    public void init() {
        currencyDAO = new CurrencyDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        try {
            response.getWriter().print(gson.toJson(currencyDAO.readAll()));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException exception) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print(createJsonExc("The database is unavailable"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        try {
            String code = request.getParameter("code");
            String fullName = request.getParameter("full_name");
            String sign = request.getParameter("sign");

            if (code == null || fullName == null || sign == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(createJsonExc("The required form field is missing"));
                return;
            }

            if (currencyDAO.isExistCode(code)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().print(createJsonExc("A currency with this code already exists"));
                return;
            }

            Сurrency createdCurrency = currencyDAO.create(new CurrencyDTO(code, fullName, sign));
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(gson.toJson(createdCurrency));
        }catch (SQLException exception){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print(createJsonExc("The database is unavailable"));
        }
    }

    private String createJsonExc(String message) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", message);

        return gson.toJson(errorMap);
    }
}