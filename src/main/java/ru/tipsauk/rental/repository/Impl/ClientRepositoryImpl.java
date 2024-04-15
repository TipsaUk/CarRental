package ru.tipsauk.rental.repository.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tipsauk.rental.entity.Client;
import ru.tipsauk.rental.entity.Document;
import ru.tipsauk.rental.exception.EntityCreateException;
import ru.tipsauk.rental.exception.EntityOperationException;
import ru.tipsauk.rental.exception.EntityUpdateException;
import ru.tipsauk.rental.repository.ClientRepository;
import ru.tipsauk.rental.repository.DatabaseService;
import ru.tipsauk.rental.util.RepositoryUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ClientRepositoryImpl implements ClientRepository {

    private final DatabaseService databaseService;

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = """
                SELECT
                  cl.*,
                  doc.client_id,
                  doc.series,
                  doc.number,
                  doc.valid_date AS validDate
                FROM rental.client cl
                 JOIN rental.document doc
                   ON cl.id = doc.client_id
                ORDER BY cl.name""";
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                clients.add(getClientFromResultSet(resultSet));
            }
        } catch (SQLException e) {
           log.error("FindAll error: " + e.getMessage());
           throw new EntityOperationException("FindAll error: " + e.getMessage());
        }
        return clients;
    }

    @Override
    public Client findById(long id) {
        Client client;
        String sql = """
                SELECT
                  cl.*,
                  doc.client_id,
                  doc.series,
                  doc.number,
                  doc.valid_date AS validDate
                FROM rental.client cl
                 JOIN rental.document doc
                   ON cl.id = doc.client_id
                WHERE cl.id = ?""";
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatement(sql)) {
            client = getResultQueryFindClient(id, preparedStatement);
        } catch (SQLException e) {
            log.error("FindById error. ID " + id + " error: " + e.getMessage());
            throw new EntityOperationException("FindById error. ID " + id + " error: " + e.getMessage());
        }
        if (client == null) {
            log.error("Client not found. ID " + id);
            throw new EntityOperationException("Client not found. ID " + id);
        }
        return client;
    }

    @Override
    public Client create(Client client) {
        String sql = "INSERT INTO rental.client (name, surname) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatementWithKeys(sql)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getSurname());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            log.error("Create error: " + e.getMessage());
            throw new EntityCreateException(client.toString() + " create error: " + e.getMessage());
        }
        return client;
    }
    @Override
    public Client update(Client client) {
        findById(client.getId());
        String sql = "UPDATE rental.client SET name = ?, surname = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatement(sql)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getSurname());
            preparedStatement.setLong(3, client.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Update error: " + e.getMessage());
            throw new EntityUpdateException(client + " update error: " + e.getMessage());
        }
        return client;
    }

    @Override
    public void deleteById(long id) {
        Client client = findById(id);
        log.info("Delete by ID: "+ client);
        String sql = "DELETE FROM rental.client WHERE id = ?";
        RepositoryUtils.executeDelete(id, sql, databaseService);
    }

    private Client getResultQueryFindClient(long id, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, id);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return getClientFromResultSet(resultSet);
            }
        }
        return null;
    }

    private Client getClientFromResultSet(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        long client_id = resultSet.getLong("client_id");
        String name = resultSet.getString("name");
        String surname = resultSet.getString("surname");
        String series = resultSet.getString("series");
        String number = resultSet.getString("number");
        java.sql.Timestamp timestamp = resultSet.getTimestamp("validDate");
        return new Client(id, name, surname, new Document(client_id, series, number, new Date(timestamp.getTime())));
    }

}
