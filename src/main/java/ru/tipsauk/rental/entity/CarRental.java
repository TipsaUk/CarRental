package ru.tipsauk.rental.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class CarRental {

    private long id;

    private long clientId;

    private long carId;

    private Date startDate;

    private Date endDate;

    private RentalStatus status;

}
