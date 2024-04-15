package ru.tipsauk.rental.handler.Impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.tipsauk.rental.dto.ApiResponse;
import ru.tipsauk.rental.dto.ClientDto;
import ru.tipsauk.rental.exception.EntityCreateException;
import ru.tipsauk.rental.exception.EntityOperationException;
import ru.tipsauk.rental.exception.EntityUpdateException;
import ru.tipsauk.rental.handler.ClientHandler;
import ru.tipsauk.rental.handler.ErrorHandler;
import ru.tipsauk.rental.service.ClientService;
import ru.tipsauk.rental.util.RequestUtils;


@RequiredArgsConstructor
public class ClientHandlerImpl implements ClientHandler {

    private final ClientService clientService;

    private final ObjectMapper objectMapper;


    @Override
    public void findAll(HttpServletRequest request, HttpServletResponse response) {
        RequestUtils.findAll(response, objectMapper, clientService);
    }

    @Override
    public void findById(HttpServletRequest request, HttpServletResponse response) {
        RequestUtils.findById(request, response, objectMapper, clientService);
    }

    @Override
    public void create(HttpServletRequest request, HttpServletResponse response) {
        try {
            String jsonString = RequestUtils.extractJsonFromRequest(request);
            clientService.create(objectMapper.readValue(jsonString, ClientDto.class));
            RequestUtils.setResponse(response, objectMapper.writeValueAsString(
                    new ApiResponse("SUCCESS", "The new client has been successfully added")));
        } catch (JsonParseException | JsonMappingException | EntityCreateException e) {
            ErrorHandler.createError(response, e, objectMapper);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void update(HttpServletRequest request, HttpServletResponse response) {
        try {
            String jsonString = RequestUtils.extractJsonFromRequest(request);
            clientService.update(objectMapper.readValue(jsonString, ClientDto.class));
            RequestUtils.setResponse(response, objectMapper.writeValueAsString(
                    new ApiResponse("SUCCESS", "The client has been successfully updated")));
        } catch (JsonParseException | JsonMappingException | EntityUpdateException | EntityOperationException e) {
            ErrorHandler.createError(response, e, objectMapper);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(HttpServletRequest request, HttpServletResponse response) {
        try {
            long id  = Long.parseLong(request.getParameter("id"));
            clientService.deleteById(id);
            RequestUtils.setResponse(response, objectMapper.writeValueAsString(
                    new ApiResponse("SUCCESS", "The client has been successfully deleted")));
        } catch (EntityOperationException e) {
            ErrorHandler.createError(response, e, objectMapper);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}
