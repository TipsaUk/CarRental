package ru.tipsauk.rental.repository;

import ru.tipsauk.rental.config.ApplicationConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseService {

    private final ApplicationConfig appConfig;

    private Connection connection;

    public DatabaseService(ApplicationConfig appConfig) {
        this.appConfig = appConfig;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(appConfig.getUrl(), appConfig.getUsername()
                    , appConfig.getPassword());
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void startTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
    }

    public void commitTransaction() throws SQLException {
        getConnection().commit();
        getConnection().setAutoCommit(true);
    }

    public void rollbackTransaction() {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public PreparedStatement createPreparedStatement(String sql) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка инициализации драйвера: " + e.getMessage());
        }
        Connection connection = getConnection();
        return connection.prepareStatement(sql);
    }

    public PreparedStatement createPreparedStatementWithKeys(String sql) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка инициализации драйвера: " + e.getMessage());
        }
        Connection connection = getConnection();
        return connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
    }
}
