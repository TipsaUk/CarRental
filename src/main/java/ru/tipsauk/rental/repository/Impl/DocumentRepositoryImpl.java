package ru.tipsauk.rental.repository.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tipsauk.rental.entity.Document;
import ru.tipsauk.rental.exception.EntityCreateException;
import ru.tipsauk.rental.exception.EntityUpdateException;
import ru.tipsauk.rental.repository.DatabaseService;
import ru.tipsauk.rental.repository.DocumentRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class DocumentRepositoryImpl implements DocumentRepository {

    private final DatabaseService databaseService;

    @Override
    public Document create(Document document) {
        String sql = "INSERT INTO rental.document (client_id, series, number, valid_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatement(sql)) {
            preparedStatement.setLong(1, document.getClient_id());
            preparedStatement.setString(2, document.getSeries());
            preparedStatement.setString(3, document.getNumber());
            preparedStatement.setDate(4, new Date(document.getValidDate().getTime()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Create error:: " + e.getMessage());
            throw new EntityCreateException(document.toString() + " create error: " + e.getMessage());
        }
        return document;

    }

    @Override
    public Document update(Document document) {
        String sql = "UPDATE rental.document SET series = ?, number = ?, valid_date = ? WHERE client_id = ?";
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatement(sql)) {
            preparedStatement.setString(1, document.getSeries());
            preparedStatement.setString(2, document.getNumber());
            preparedStatement.setDate(3, new Date(document.getValidDate().getTime()));
            preparedStatement.setLong(4, document.getClient_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Document update error: " + e.getMessage());
            throw new EntityUpdateException(document.toString() + " update error: " + e.getMessage());
        }
        return document;
    }
}
