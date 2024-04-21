package ru.tipsauk.rental.handler.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.tipsauk.rental.dto.ApiResponse;
import ru.tipsauk.rental.dto.CarDto;
import ru.tipsauk.rental.entity.Transmission;
import ru.tipsauk.rental.service.CarService;

import java.io.*;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CarHandlerImplTest {

    @Mock
    private CarService carService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CarHandlerImpl carHandler;

    @Mock
    private PrintWriter writer;

    private CarDto carDto;

    private String jsonRequest;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        carDto = new CarDto(1, "Toyota", "Camry"
                , "A123BC", Transmission.AUTOMATIC, "black");
        jsonRequest = "{\"id\":1,\"brand\":\"Toyota\",\"model\":\"Camry\"" +
                ",\"number\":\"A123BC\",\"transmission\":\"AUTOMATIC\",\"color\":\"black\"}";
    }

    @Test
    void findAll_WhenRetrievingAllCars_ExpectSuccessfulResponseWithCorrectContent() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(carService.findAll()).thenReturn(Collections.emptyList());
        when(objectMapper.writeValueAsString(Collections.emptyList())).thenReturn("expectedResponse");

        carHandler.findAll(request, response);

        verify(carService).findAll();
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getWriter();
        verify(response.getWriter()).write("expectedResponse");
    }

    @Test
    void findById_WhenRetrievingCarById_ExpectSuccessfulResponseWithCorrectContent() throws IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(writer);
        when(carService.findById(1)).thenReturn(carDto);
        when(objectMapper.writeValueAsString(carDto)).thenReturn("");

        carHandler.findById(request, response);

        verify(carService).findById(1);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getWriter();
        verify(response.getWriter()).write("");
    }

    @Test
    void create_WhenCreatingCar_ExpectSuccessfulResponseWithCorrectContent() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(writer);
        when(objectMapper.readValue(jsonRequest, CarDto.class)).thenReturn(carDto);
        when(objectMapper.writeValueAsString(any(ApiResponse.class))).thenReturn("expectedResponse");

        carHandler.create(request, response);

        verify(carService).create(carDto);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response.getWriter()).write("expectedResponse");
    }

    @Test
    void update_WhenUpdatingCar_ExpectSuccessfulResponseWithCorrectContent() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(writer);
        when(objectMapper.readValue(jsonRequest, CarDto.class)).thenReturn(carDto);
        when(objectMapper.writeValueAsString(any(ApiResponse.class))).thenReturn("expectedResponse");

        carHandler.update(request, response);

        verify(carService).update(carDto);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response.getWriter()).write("expectedResponse");
    }

    @Test
    void deleteById_WhenDeletingCarById_ExpectSuccessfulResponseWithCorrectContent() throws IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(writer);
        when(objectMapper.writeValueAsString(any(ApiResponse.class))).thenReturn("expectedResponse");

        carHandler.deleteById(request, response);

        verify(carService).deleteById(1);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write("expectedResponse");
    }
}