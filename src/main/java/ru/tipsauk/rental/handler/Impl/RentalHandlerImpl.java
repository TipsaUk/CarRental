package ru.tipsauk.rental.handler.Impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.tipsauk.rental.dto.ApiResponse;
import ru.tipsauk.rental.dto.CarRentalDto;
import ru.tipsauk.rental.exception.EntityCreateException;
import ru.tipsauk.rental.exception.EntityOperationException;
import ru.tipsauk.rental.exception.EntityUpdateException;
import ru.tipsauk.rental.handler.ErrorHandler;
import ru.tipsauk.rental.handler.RentalHandler;
import ru.tipsauk.rental.service.RentalService;
import ru.tipsauk.rental.util.RequestUtils;

@RequiredArgsConstructor
public class RentalHandlerImpl implements RentalHandler {

    private final RentalService rentalService;

    private final ObjectMapper objectMapper;

    @Override
    public void findAll(HttpServletRequest request, HttpServletResponse response) {
        RequestUtils.findAll(response, objectMapper, rentalService);
    }

    @Override
    public void findById(HttpServletRequest request, HttpServletResponse response) {
        RequestUtils.findById(request, response, objectMapper, rentalService);
    }

    @Override
    public void create(HttpServletRequest request, HttpServletResponse response) {
        try {
            String jsonString = RequestUtils.extractJsonFromRequest(request);
            rentalService.create(objectMapper.readValue(jsonString, CarRentalDto.class));
            RequestUtils.setResponse(response, objectMapper.writeValueAsString(
                    new ApiResponse("SUCCESS", "The new car rental has been successfully added")));
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
            rentalService.update(objectMapper.readValue(jsonString, CarRentalDto.class));
            RequestUtils.setResponse(response, objectMapper.writeValueAsString(
                    new ApiResponse("SUCCESS", "The car rental has been successfully updated")));
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
            long id  = RequestUtils.getIdFromRequest(request);
            rentalService.deleteById(id);
            RequestUtils.setResponse(response, objectMapper.writeValueAsString(
                    new ApiResponse("SUCCESS", "The car rental has been successfully deleted")));
        } catch (EntityOperationException e) {
            ErrorHandler.createError(response, e, objectMapper);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}
