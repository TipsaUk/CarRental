package ru.tipsauk.rental.controller.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.tipsauk.rental.handler.ClientHandler;
import java.io.IOException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClientServletTest {

    private ClientHandler clientHandler;
    private ClientServlet clientServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        clientHandler = Mockito.mock(ClientHandler.class);
        clientServlet = new ClientServlet();
        ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
        ServletContext servletContext = Mockito.mock(ServletContext.class);
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("clientHandler")).thenReturn(clientHandler);
        try {
            clientServlet.init(servletConfig);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("Тест для get - запроса без параметра")
    public void testDoGetWithoutIdParameter() throws ServletException, IOException {
        clientServlet.doGet(request, response);
        verify(clientHandler).findAll(request, response);
    }

    @Test
    @DisplayName("Тест для get - запроса с параметром id")
    public void testDoGetWithIdParameter() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("1");
        clientServlet.doGet(request, response);
        verify(clientHandler).findById(request, response);
    }

    @Test
    @DisplayName("Тест для post - запроса")
    public void testDoPost() throws ServletException, IOException {
        clientServlet.doPost(request, response);
        verify(clientHandler).create(request, response);
    }

    @Test
    @DisplayName("Тест для put - запроса")
    public void testDoPut() throws ServletException, IOException {
        clientServlet.doPut(request, response);
        verify(clientHandler).update(request, response);
    }

    @Test
    @DisplayName("Тест для delete - запроса")
    public void testDoDelete() throws ServletException, IOException {
        clientServlet.doDelete(request, response);
        verify(clientHandler).deleteById(request, response);
    }

}