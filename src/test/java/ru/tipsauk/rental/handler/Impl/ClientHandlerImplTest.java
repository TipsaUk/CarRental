package ru.tipsauk.rental.handler.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.tipsauk.rental.dto.ApiResponse;
import ru.tipsauk.rental.dto.ClientDto;
import ru.tipsauk.rental.dto.DocumentDto;
import ru.tipsauk.rental.service.ClientService;

import java.io.*;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientHandlerImplTest {

    @Mock
    private ClientService clientService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ClientHandlerImpl clientHandler;

    @Mock
    PrintWriter writer;

    ClientDto clientDto = new ClientDto(1, "Иван", "Иванов",
            new DocumentDto(1, "5555", "666666", new Date()));

    String jsonRequest = "{\"id\":1,\"firstName\":\"Иван\",\"lastName\":\"Иванов\"" +
            ",\"document\":{\"id\":1,\"series\":\"5555\",\"number\":\"666666\",\"validDate\":\"2024-04-13\"}}";

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Получение списка всех клиентов")
    void findAll() throws IOException {
        when(response.getWriter()).thenReturn(writer);
        when(clientService.findAll()).thenReturn(Collections.emptyList());
        when(objectMapper.writeValueAsString(Collections.emptyList())).thenReturn("expectedResponse");
        clientHandler.findAll(request, response);
        verify(clientService).findAll();
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getWriter();
        verify(response.getWriter()).write("expectedResponse");
    }

    @Test
    @DisplayName("Получение клиента по id")
    void findById() throws IOException {
        when(request.getParameter("id")).thenReturn("1");
        when(response.getWriter()).thenReturn(writer);
        when(clientService.findById(1L)).thenReturn(clientDto);
        when(objectMapper.writeValueAsString(clientDto)).thenReturn("");
        clientHandler.findById(request, response);
        verify(clientService).findById(1L);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getWriter();
        verify(response.getWriter()).write("");
    }

    @Test
    @DisplayName("Создание клиента")
    void create() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(writer);
        when(objectMapper.readValue(jsonRequest, ClientDto.class)).thenReturn(clientDto);
        when(objectMapper.writeValueAsString(any(ApiResponse.class))).thenReturn("expectedResponse");
        clientHandler.create(request, response);
        verify(clientService).create(clientDto);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response.getWriter()).write("expectedResponse");
    }

    @Test
    @DisplayName("Обновление клиента")
    void update() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(writer);
        when(objectMapper.readValue(jsonRequest, ClientDto.class)).thenReturn(clientDto);
        when(objectMapper.writeValueAsString(any(ApiResponse.class))).thenReturn("expectedResponse");
        clientHandler.update(request, response);
        verify(clientService).update(clientDto);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response.getWriter()).write("expectedResponse");
    }

    @Test
    @DisplayName("Удаление клиента")
    void deleteById() throws IOException {
        when(request.getParameter("id")).thenReturn("1");
        when(response.getWriter()).thenReturn(writer);
        when(objectMapper.writeValueAsString(any(ApiResponse.class))).thenReturn("expectedResponse");
        clientHandler.deleteById(request, response);
        verify(clientService).deleteById(1L);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write("expectedResponse");
    }
}