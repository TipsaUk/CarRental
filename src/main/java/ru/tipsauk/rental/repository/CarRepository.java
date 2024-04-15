package ru.tipsauk.rental.repository;

import ru.tipsauk.rental.entity.Car;

import java.util.List;

public interface CarRepository {

    List<Car> findAll();

    Car findById(long id);

    Car create(Car car);

    Car update(Car car);

    void deleteById(long id);
}
