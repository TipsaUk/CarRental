package ru.tipsauk.rental.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import ru.tipsauk.rental.exception.EntityOperationException;
import ru.tipsauk.rental.repository.DatabaseService;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class RepositoryUtils {

    public static void executeDelete(long id, String sql, DatabaseService databaseService) {
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("DeleteById error. ID " + id + " error: " + e.getMessage());
            throw new EntityOperationException("DeleteById error. ID " + id + " error: " + e.getMessage());
        }
    }

}
