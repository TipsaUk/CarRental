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
    public void doGet_WithoutIdParameter_AllCarRentalsReturned() throws ServletException, IOException {
        rentalServlet.doGet(request, response);
        verify(rentalHandler).findAll(request, response);
    }

    @Test
    public void doGet_WithIdParameter_CarRentalByIdReturned() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        rentalServlet.doGet(request, response);
        verify(rentalHandler).findById(request, response);
    }

    @Test
    public void doPost_CarRentalCreated() throws ServletException, IOException {
        rentalServlet.doPost(request, response);
        verify(rentalHandler).create(request, response);
    }

    @Test
    public void doPut_CarRentalUpdated() throws ServletException, IOException {
        rentalServlet.doPut(request, response);
        verify(rentalHandler).update(request, response);
    }

    @Test
    public void doDelete_CarRentalDeleted() throws ServletException, IOException {
        rentalServlet.doDelete(request, response);
        verify(rentalHandler).deleteById(request, response);
    }

}