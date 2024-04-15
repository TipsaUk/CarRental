package ru.tipsauk.rental.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Car {

    private long id;

    private String brand;

    private String model;

    private String number;

    private Transmission transmission;

    private String color;

}
