package ru.tipsauk.rental.handler.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.tipsauk.rental.dto.ApiResponse;
import ru.tipsauk.rental.dto.CarRentalDto;
import ru.tipsauk.rental.entity.RentalStatus;
import ru.tipsauk.rental.service.RentalService;

import java.io.*;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RentalHandlerImplTest {

    @Mock
    private RentalService rentalService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RentalHandlerImpl rentalHandler;

    @Mock
    PrintWriter writer;

    CarRentalDto carRentalDto = new CarRentalDto(1, 1, 1, new Date(), new Date(), RentalStatus.BOOKED);

    String jsonRequest = "{\"id\":1,\"clientId\":\"1\",\"carId\":\"1\"" +
            ",\"startDate\":\"2024-04-13\",\"endDate\":\"2024-04-13\",\"status\":\"BOOKED\"}";

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_WhenRetrievingAllCarRentals_ExpectSuccessfulResponseWithCorrectContent() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(rentalService.findAll()).thenReturn(Collections.emptyList());
        when(objectMapper.writeValueAsString(Collections.emptyList())).thenReturn("expectedResponse");

        rentalHandler.findAll(request, response);

        verify(rentalService).findAll();
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getWriter();
        verify(response.getWriter()).write("expectedResponse");
    }

    @Test
    void findById_WhenRetrievingCarRentalById_ExpectSuccessfulResponseWithCorrectContent() throws IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(writer);
        when(rentalService.findById(1)).thenReturn(carRentalDto);
        when(objectMapper.writeValueAsString(carRentalDto)).thenReturn("");

        rentalHandler.findById(request, response);

        verify(rentalService).findById(1);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getWriter();
        verify(response.getWriter()).write("");
    }

    @Test
    void create_WhenCreatingCarRental_ExpectSuccessfulResponseWithCorrectContent() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(writer);
        when(objectMapper.readValue(jsonRequest, CarRentalDto.class)).thenReturn(carRentalDto);
        when(objectMapper.writeValueAsString(any(ApiResponse.class))).thenReturn("expectedResponse");

        rentalHandler.create(request, response);

        verify(rentalService).create(carRentalDto);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response.getWriter()).write("expectedResponse");
    }

    @Test
    void update_WhenUpdatingCarRental_ExpectSuccessfulResponseWithCorrectContent() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(writer);
        when(objectMapper.readValue(jsonRequest, CarRentalDto.class)).thenReturn(carRentalDto);
        when(objectMapper.writeValueAsString(any(ApiResponse.class))).thenReturn("expectedResponse");

        rentalHandler.update(request, response);

        verify(rentalService).update(carRentalDto);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response.getWriter()).write("expectedResponse");
    }

    @Test
    void deleteById_WhenDeletingCarRentalById_ExpectSuccessfulResponseWithCorrectContent() throws IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(writer);
        when(objectMapper.writeValueAsString(any(ApiResponse.class))).thenReturn("expectedResponse");

        rentalHandler.deleteById(request, response);

        verify(rentalService).deleteById(1);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write("expectedResponse");
    }
}