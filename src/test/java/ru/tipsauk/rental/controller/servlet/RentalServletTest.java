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
import ru.tipsauk.rental.handler.RentalHandler;
import java.io.IOException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RentalServletTest {

    private RentalHandler rentalHandler;
    private RentalServlet rentalServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        rentalHandler = Mockito.mock(RentalHandler.class);
        rentalServlet = new RentalServlet();
        ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
        ServletContext servletContext = Mockito.mock(ServletContext.class);
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("rentalHandler")).thenReturn(rentalHandler);
        try {
            rentalServlet.init(servletConfig);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("Тест для get - запроса без параметра")
    public void testDoGetWithoutIdParameter() throws ServletException, IOException {
        rentalServlet.doGet(request, response);
        verify(rentalHandler).findAll(request, response);
    }

    @Test
    @DisplayName("Тест для get - запроса с параметром id")
    public void testDoGetWithIdParameter() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("1");
        rentalServlet.doGet(request, response);
        verify(rentalHandler).findById(request, response);
    }

    @Test
    @DisplayName("Тест для post - запроса")
    public void testDoPost() throws ServletException, IOException {
        rentalServlet.doPost(request, response);
        verify(rentalHandler).create(request, response);
    }

    @Test
    @DisplayName("Тест для put - запроса")
    public void testDoPut() throws ServletException, IOException {
        rentalServlet.doPut(request, response);
        verify(rentalHandler).update(request, response);
    }

    @Test
    @DisplayName("Тест для delete - запроса")
    public void testDoDelete() throws ServletException, IOException {
        rentalServlet.doDelete(request, response);
        verify(rentalHandler).deleteById(request, response);
    }

}