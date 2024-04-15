package ru.tipsauk.rental.controller.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tipsauk.rental.handler.ClientHandler;

import java.io.IOException;

@WebServlet(
        name = "ClientServlet",
        loadOnStartup = 2,
        description = "Servlet for clients",
        urlPatterns = {"/client"}
)
public class ClientServlet extends HttpServlet {

    private ClientHandler clientHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        clientHandler = (ClientHandler) config.getServletContext().getAttribute("clientHandler");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("id") != null) {
            clientHandler.findById(req, resp);
        } else {
            clientHandler.findAll(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        clientHandler.create(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        clientHandler.update(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        clientHandler.deleteById(req, resp);
    }


}
