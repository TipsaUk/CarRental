package ru.tipsauk.rental.controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import ru.tipsauk.rental.config.ApplicationConfig;
import ru.tipsauk.rental.handler.CarHandler;
import ru.tipsauk.rental.handler.ClientHandler;
import ru.tipsauk.rental.handler.Impl.CarHandlerImpl;
import ru.tipsauk.rental.handler.Impl.ClientHandlerImpl;
import ru.tipsauk.rental.handler.Impl.RentalHandlerImpl;
import ru.tipsauk.rental.handler.RentalHandler;
import ru.tipsauk.rental.mapper.CarMapper;
import ru.tipsauk.rental.mapper.CarRentalMapper;
import ru.tipsauk.rental.mapper.ClientMapper;
import ru.tipsauk.rental.mapper.DocumentMapper;
import ru.tipsauk.rental.repository.*;
import ru.tipsauk.rental.repository.Impl.CarRepositoryImpl;
import ru.tipsauk.rental.repository.Impl.ClientRepositoryImpl;
import ru.tipsauk.rental.repository.Impl.DocumentRepositoryImpl;
import ru.tipsauk.rental.repository.Impl.RentalRepositoryImpl;
import ru.tipsauk.rental.service.CarService;
import ru.tipsauk.rental.service.ClientService;
import ru.tipsauk.rental.service.DocumentService;
import ru.tipsauk.rental.service.Impl.CarServiceImpl;
import ru.tipsauk.rental.service.Impl.ClientServiceImpl;
import ru.tipsauk.rental.service.Impl.DocumentServiceImpl;
import ru.tipsauk.rental.service.Impl.RentalServiceImpl;
import ru.tipsauk.rental.service.RentalService;

@WebServlet(
        name = "InitServletContext",
        loadOnStartup = 1,
        description = "Servlet for initialization",
        urlPatterns = {"/init"}
)
public class InitServletContext extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ObjectMapper objectMapper = new ObjectMapper();

        ApplicationConfig appConfig = new ApplicationConfig();
        ClientMapper clientMapper = ClientMapper.INSTANCE;
        DocumentMapper documentMapper = DocumentMapper.INSTANCE;
        CarRentalMapper carRentalMapper = CarRentalMapper.INSTANCE;
        CarMapper carMapper = CarMapper.INSTANCE;

        DatabaseService databaseService = new DatabaseService(appConfig);
        ClientRepository clientRepository = new ClientRepositoryImpl(databaseService);
        DocumentRepository documentRepository = new DocumentRepositoryImpl(databaseService);
        RentalRepository rentalRepository = new RentalRepositoryImpl(databaseService);
        CarRepository carRepository = new CarRepositoryImpl(databaseService);

        DocumentService documentService = new DocumentServiceImpl(documentRepository, documentMapper);
        ClientService clientService = new ClientServiceImpl(clientRepository, documentService, clientMapper);
        RentalService rentalService = new RentalServiceImpl(rentalRepository, carRentalMapper);
        CarService carService = new CarServiceImpl(carRepository, carMapper);

        ClientHandler clientHandler = new ClientHandlerImpl(clientService, objectMapper);
        RentalHandler rentalHandler = new RentalHandlerImpl(rentalService, objectMapper);
        CarHandler carHandler = new CarHandlerImpl(carService, objectMapper);

        config.getServletContext().setAttribute("clientHandler", clientHandler);
        config.getServletContext().setAttribute("rentalHandler", rentalHandler);
        config.getServletContext().setAttribute("carHandler", carHandler);
    }
}
