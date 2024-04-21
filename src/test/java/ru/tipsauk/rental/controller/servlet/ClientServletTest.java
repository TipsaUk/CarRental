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
    public void doGet_WithoutIdParameter_AllClientsReturned() throws ServletException, IOException {
        clientServlet.doGet(request, response);
        verify(clientHandler).findAll(request, response);
    }

    @Test
    public void doGet_WithIdParameter_ClientByIdReturned() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        clientServlet.doGet(request, response);
        verify(clientHandler).findById(request, response);
    }

    @Test
    public void doPost_ClientCreated() throws ServletException, IOException {
        clientServlet.doPost(request, response);
        verify(clientHandler).create(request, response);
    }

    @Test
    public void doPut_ClientUpdated() throws ServletException, IOException {
        clientServlet.doPut(request, response);
        verify(clientHandler).update(request, response);
    }

    @Test
    public void doDelete_ClientDeleted() throws ServletException, IOException {
        clientServlet.doDelete(request, response);
        verify(clientHandler).deleteById(request, response);
    }

}