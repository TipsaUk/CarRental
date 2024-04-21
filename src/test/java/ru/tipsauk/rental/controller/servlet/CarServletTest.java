package ru.tipsauk.rental.controller.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tipsauk.rental.handler.CarHandler;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CarServletTest {

    private CarHandler carHandler;
    private CarServlet carServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        carHandler = Mockito.mock(CarHandler.class);
        carServlet = new CarServlet();
        ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
        ServletContext servletContext = Mockito.mock(ServletContext.class);
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("carHandler")).thenReturn(carHandler);
        try {
            carServlet.init(servletConfig);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
    }

    @Test
    public void doGet_WithoutIdParameter_AllCarsReturned() throws ServletException, IOException {
        carServlet.doGet(request, response);
        verify(carHandler).findAll(request, response);
    }

    @Test
    public void doGet_WithIdParameter_CarByIdReturned() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        carServlet.doGet(request, response);
        verify(carHandler).findById(request, response);
    }

    @Test
    public void doPost_CarCreated() throws ServletException, IOException {
        carServlet.doPost(request, response);
        verify(carHandler).create(request, response);
    }

    @Test
    public void doPut_CarUpdated() throws ServletException, IOException {
        carServlet.doPut(request, response);
        verify(carHandler).update(request, response);
    }

    @Test
    public void doDelete_CarDeleted() throws ServletException, IOException {
        carServlet.doDelete(request, response);
        verify(carHandler).deleteById(request, response);
    }

}