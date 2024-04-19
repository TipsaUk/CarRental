package ru.tipsauk.rental.repository;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.tipsauk.rental.config.ApplicationConfig;
import ru.tipsauk.rental.entity.Client;
import ru.tipsauk.rental.entity.Document;
import ru.tipsauk.rental.repository.Impl.CarRepositoryImpl;
import ru.tipsauk.rental.repository.Impl.ClientRepositoryImpl;
import ru.tipsauk.rental.repository.Impl.DocumentRepositoryImpl;
import ru.tipsauk.rental.repository.Impl.RentalRepositoryImpl;

import java.sql.*;
import java.util.Properties;


@Testcontainers
public abstract class BaseDatabaseTest {

    @Container
    protected static final PostgreSQLContainer<?> SQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("car_rental_test")
            .withUsername("rent")
            .withPassword("car");
    protected static DatabaseService databaseService;
    protected static CarRepository carRepository;
    protected static ClientRepository clientRepository;
    protected static DocumentRepository documentRepository;
    protected static RentalRepository rentalRepository;

    protected static void baseSetUp() {
        try  {
            SQLContainer.start();
            Properties liquibaseProperties = new Properties();
            liquibaseProperties.load(BaseDatabaseTest.class
                    .getClassLoader().getResourceAsStream("application.properties"));
            Connection connection = DriverManager.getConnection(SQLContainer.getJdbcUrl()
                    , SQLContainer.getUsername(), SQLContainer.getPassword());
            String changeLogFile = liquibaseProperties.getProperty("testChangeLogFile");
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changeLogFile,
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update("test");
            connection.close();

            ApplicationConfig appConfig = new ApplicationConfig(SQLContainer.getJdbcUrl()
                    , SQLContainer.getUsername(), SQLContainer.getPassword());

            databaseService = new DatabaseService(appConfig);
            carRepository = new CarRepositoryImpl(databaseService);
            clientRepository = new ClientRepositoryImpl(databaseService);
            documentRepository = new DocumentRepositoryImpl(databaseService);
            rentalRepository = new RentalRepositoryImpl(databaseService);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected static void baseTearDown() {
        if (SQLContainer != null && SQLContainer.isRunning()) {
            SQLContainer.stop();
        }
    }

    protected static void clearTable(String nameTable) {
        try (PreparedStatement preparedStatement = databaseService
                .createPreparedStatement("DELETE FROM rental." + nameTable)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Ошибка настройки теста: " + e.getMessage(), e);
        }
    }

    protected static void createClient(Client client) {
        Client newClient1 = clientRepository.create(client);
        Document document1 = client.getDocument();
        document1.setClient_id(newClient1.getId());
        documentRepository.create(document1);
    }

}
