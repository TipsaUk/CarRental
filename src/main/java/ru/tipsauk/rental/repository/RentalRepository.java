package ru.tipsauk.rental.repository;

import ru.tipsauk.rental.entity.CarRental;

import java.util.List;

public interface RentalRepository {

    List<CarRental> findAll();

    CarRental findById(long id);

    CarRental create(CarRental carRental);

    CarRental update(CarRental carRental);

    void deleteById(long id);

}
