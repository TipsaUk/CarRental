package ru.tipsauk.rental.controller.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tipsauk.rental.handler.CarHandler;

import java.io.IOException;

@WebServlet(
        name = "CarServlet",
        loadOnStartup = 4,
        description = "Servlet for car",
        urlPatterns = {"/car"}
)
public class CarServlet extends HttpServlet {

    CarHandler carHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        carHandler = (CarHandler) config.getServletContext().getAttribute("carHandler");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") != null) {
            carHandler.findById(req, resp);
        } else {
            carHandler.findAll(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        carHandler.create(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        carHandler.update(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        carHandler.deleteById(req, resp);
    }
}
