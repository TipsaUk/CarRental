package ru.tipsauk.rental.repository.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import ru.tipsauk.rental.entity.*;
import ru.tipsauk.rental.exception.EntityCreateException;
import ru.tipsauk.rental.exception.EntityOperationException;
import ru.tipsauk.rental.exception.EntityUpdateException;
import ru.tipsauk.rental.repository.DatabaseService;
import ru.tipsauk.rental.repository.RentalRepository;
import ru.tipsauk.rental.util.RepositoryUtils;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class RentalRepositoryImpl implements RentalRepository {

    private final DatabaseService databaseService;

    @Override
    public List<CarRental> findAll() {
        List<CarRental> carRentals = new ArrayList<>();
        String sql = """
                SELECT
                  cr.*
                FROM rental.car_rental cr
                ORDER BY cr.start_date""";
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                carRentals.add(getRentalFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            log.error("FindAll error: " + e.getMessage());
            throw new EntityOperationException("FindAll error: " + e.getMessage());
        }
        return carRentals;
    }

    @Override
    public CarRental findById(long id) {
        CarRental carRental;
        String sql = """
                SELECT
                  cr.*
                FROM rental.car_rental cr
                WHERE cr.id = ?""";
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatement(sql)) {
            carRental = getResultQueryFindRental(id, preparedStatement);
        } catch (SQLException e) {
            log.error("FindById error. ID " + id + " error: " + e.getMessage());
            throw new EntityOperationException("FindById error. ID " + id + " error: " + e.getMessage());
        }
        if (carRental == null) {
            log.error("Car rental not found. ID " + id);
            throw new EntityOperationException("Car rental not found. ID " + id);
        }
        return carRental;
    }

    @Override
    public CarRental create(CarRental carRental) {
        String sql = """
                INSERT INTO rental.car_rental (client_id, car_id, start_date, end_date, status)
                VALUES (?, ?, ?, ?, ?)""";
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatementWithKeys(sql)) {
            preparedStatement.setLong(1, carRental.getClientId());
            preparedStatement.setLong(2, carRental.getCarId());
            preparedStatement.setDate(3, new Date(carRental.getStartDate().getTime()));
            preparedStatement.setDate(4, new Date(carRental.getEndDate().getTime()));
            preparedStatement.setString(5, carRental.getStatus().toString());

            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    carRental.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            log.error("Create error: " + e.getMessage());
            throw new EntityCreateException(carRental.toString() + " create error: " + e.getMessage());
        }
        return carRental;
    }

    @Override
    public CarRental update(CarRental carRental) {
        findById(carRental.getId());
        String sql = """
                UPDATE rental.car_rental SET car_id = ?, start_date = ?, end_date = ?, status = ?
                WHERE id = ?""";
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatement(sql)) {
            preparedStatement.setLong(1, carRental.getCarId());
            preparedStatement.setDate(2, new Date(carRental.getStartDate().getTime()));
            preparedStatement.setDate(3, new Date(carRental.getEndDate().getTime()));
            preparedStatement.setString(4, carRental.getStatus().toString());
            preparedStatement.setLong(5, carRental.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Update error: " + e.getMessage());
            throw new EntityUpdateException(carRental + " update error: " + e.getMessage());
        }
        return carRental;
    }

    @Override
    public void deleteById(long id) {
        CarRental carRental = findById(id);
        log.info("Delete by ID: "+ carRental);
        String sql = "DELETE FROM rental.car_rental WHERE id = ?";
        RepositoryUtils.executeDelete(id, sql, databaseService);
    }

    private CarRental getResultQueryFindRental(long id, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, id);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return getRentalFromResultSet(resultSet);
            }
        }
        return null;
    }

    private CarRental getRentalFromResultSet(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        long client_id = resultSet.getLong("client_id");
        long car_id = resultSet.getLong("car_id");
        java.sql.Timestamp startDate = resultSet.getTimestamp("start_date");
        java.sql.Timestamp endDate = resultSet.getTimestamp("end_date");
        String status = resultSet.getString("status");
        return new CarRental(id, client_id, car_id, new Date(startDate.getTime())
                , new Date(endDate.getTime()), RentalStatus.valueOf(status));
    }

}
