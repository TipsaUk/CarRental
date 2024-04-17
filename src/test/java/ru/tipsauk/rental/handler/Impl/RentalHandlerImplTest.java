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

@DisplayName("Тесты для RentalHandlerImpl")
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
    @DisplayName("Получение списка аренды машин")
    void findAll() throws IOException {
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
    @DisplayName("Получение аренды машины по id")
    void findById() throws IOException {
        when(request.getParameter("id")).thenReturn("1");
        when(response.getWriter()).thenReturn(writer);
        when(rentalService.findById(1L)).thenReturn(carRentalDto);
        when(objectMapper.writeValueAsString(carRentalDto)).thenReturn("");
        rentalHandler.findById(request, response);
        verify(rentalService).findById(1L);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getWriter();
        verify(response.getWriter()).write("");
    }

    @Test
    @DisplayName("Создание аренды машины")
    void create() throws IOException {
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
    @DisplayName("Обновление аренды машины")
    void update() throws IOException {
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
    @DisplayName("Удаление аренды машины")
    void deleteById() throws IOException {
        when(request.getParameter("id")).thenReturn("1");
        when(response.getWriter()).thenReturn(writer);
        when(objectMapper.writeValueAsString(any(ApiResponse.class))).thenReturn("expectedResponse");
        rentalHandler.deleteById(request, response);
        verify(rentalService).deleteById(1L);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write("expectedResponse");
    }
}