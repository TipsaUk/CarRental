package ru.tipsauk.rental.controller.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tipsauk.rental.handler.RentalHandler;

import java.io.IOException;

@WebServlet(
        name = "RentalServlet",
        loadOnStartup = 3,
        description = "Servlet for rental",
        urlPatterns = {"/rentals/*"}
)
public class RentalServlet extends HttpServlet {

    private RentalHandler rentalHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        rentalHandler = (RentalHandler) config.getServletContext().getAttribute("rentalHandler");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.startsWith("/")) {
            rentalHandler.findById(req, resp);
        } else {
            rentalHandler.findAll(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        rentalHandler.create(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        rentalHandler.update(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        rentalHandler.deleteById(req, resp);
    }
}
