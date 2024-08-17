package com.nastya.util;

import com.google.gson.Gson;
import com.nastya.exception.AppException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    private static final Gson gson = new Gson();

    public static void sendException(HttpServletResponse response, AppException exception)
            throws IOException {

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", exception.getMessage());

        send(response, exception.getStatus(), errorMap);
    }

    public static void send(HttpServletResponse response, int status, Object object) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        response.getWriter().print(gson.toJson(object));
    }
}
