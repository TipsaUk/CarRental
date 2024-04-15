package ru.tipsauk.rental.repository.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tipsauk.rental.entity.Car;

import ru.tipsauk.rental.entity.Transmission;
import ru.tipsauk.rental.exception.EntityCreateException;
import ru.tipsauk.rental.exception.EntityOperationException;
import ru.tipsauk.rental.exception.EntityUpdateException;
import ru.tipsauk.rental.repository.CarRepository;
import ru.tipsauk.rental.repository.DatabaseService;
import ru.tipsauk.rental.util.RepositoryUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CarRepositoryImpl implements CarRepository {

    private final DatabaseService databaseService;

    @Override
    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();
        String sql = """
                SELECT
                  *
                FROM rental.car
                ORDER BY number""";
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                cars.add(getCarFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            log.error("FindAll error: " + e.getMessage());
            throw new EntityOperationException("FindAll error: " + e.getMessage());
        }
        return cars;
    }

    @Override
    public Car findById(long id) {
        Car car;
        String sql = """
                SELECT
                  *
                FROM rental.car
                WHERE id = ?""";
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatement(sql)) {
            car = getResultQueryFindCar(id, preparedStatement);
        } catch (SQLException e) {
            log.error("FindById error. ID " + id + " error: " + e.getMessage());
            throw new EntityOperationException("FindById error. ID " + id + " error: " + e.getMessage());
        }
        if (car == null) {
            log.error("Car not found. ID " + id);
            throw new EntityOperationException("Car not found. ID " + id);
        }
        return car;
    }

    @Override
    public Car create(Car car) {
        String sql = """
                INSERT INTO rental.car (brand, model, number, transmission, color)
                VALUES (?, ?, ?, ?, ?)""";
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatementWithKeys(sql)) {
            preparedStatement.setString(1, car.getBrand());
            preparedStatement.setString(2, car.getModel());
            preparedStatement.setString(3, car.getNumber());
            preparedStatement.setString(4, car.getTransmission().toString());
            preparedStatement.setString(5, car.getColor());

            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    car.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            log.error("Create error: " + e.getMessage());
            throw new EntityCreateException(car.toString() + " create error: " + e.getMessage());
        }
        return car;
    }

    @Override
    public Car update(Car car) {
        findById(car.getId());
        String sql = """
                UPDATE rental.car SET brand = ?, model = ?, number = ?, transmission = ?, color = ?
                WHERE id = ?""";
        try (PreparedStatement preparedStatement = databaseService.createPreparedStatement(sql)) {
            preparedStatement.setString(1, car.getBrand());
            preparedStatement.setString(2, car.getModel());
            preparedStatement.setString(3, car.getNumber());
            preparedStatement.setString(4, car.getTransmission().toString());
            preparedStatement.setString(5, car.getColor());
            preparedStatement.setLong(6, car.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Update error: " + e.getMessage());
            throw new EntityUpdateException(car + " update error: " + e.getMessage());
        }
        return car;
    }

    @Override
    public void deleteById(long id) {
        Car car = findById(id);
        log.info("Delete by ID: "+ car);
        String sql = "DELETE FROM rental.car WHERE id = ?";
        RepositoryUtils.executeDelete(id, sql, databaseService);
    }

    private Car getResultQueryFindCar(long id, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, id);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return getCarFromResultSet(resultSet);
            }
        }
        return null;
    }

    private Car getCarFromResultSet(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String brand = resultSet.getString("brand");
        String model = resultSet.getString("model");
        String number = resultSet.getString("number");
        String transmission = resultSet.getString("transmission");
        String color = resultSet.getString("color");
        return new Car(id, brand, model, number, Transmission.valueOf(transmission), color);
    }
}
