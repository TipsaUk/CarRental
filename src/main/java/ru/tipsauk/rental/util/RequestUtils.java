package ru.tipsauk.rental.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tipsauk.rental.exception.EntityOperationException;
import ru.tipsauk.rental.handler.ErrorHandler;
import ru.tipsauk.rental.service.EntityService;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestUtils {

    public static void setResponse(HttpServletResponse response, String jsonResponse) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public static String extractJsonFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }
        return jsonBuilder.toString();
    }

    public static void findAll(HttpServletResponse response, ObjectMapper objectMapper, EntityService entityService) {
        try {
            String jsonResponse = objectMapper.writeValueAsString(entityService.findAll());
            setResponse(response, jsonResponse);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    public static void findById(HttpServletRequest request, HttpServletResponse response
            , ObjectMapper objectMapper, EntityService entityService) {
        try {
            long id  = Long.parseLong(request.getParameter("id"));
            String jsonResponse = objectMapper.writeValueAsString(entityService.findById(id));
            setResponse(response, jsonResponse);
        } catch (EntityOperationException e) {
            ErrorHandler.createError(response, e, objectMapper);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }


}
