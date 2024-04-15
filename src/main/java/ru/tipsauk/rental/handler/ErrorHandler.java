package ru.tipsauk.rental.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.tipsauk.rental.dto.ApiResponse;
import ru.tipsauk.rental.util.RequestUtils;

import java.io.IOException;

@Slf4j
public class ErrorHandler {

    public static void createError(HttpServletResponse response, Throwable e, ObjectMapper objectMapper) {
        try {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            RequestUtils.setResponse(response, objectMapper.writeValueAsString(
                    new ApiResponse("ERROR", e.getMessage())));
        } catch (IOException ioException) {
            log.error("Error is acquired in createError", ioException);
        }
    }

}
