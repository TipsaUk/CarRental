package ru.tipsauk.rental.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Document {

    private long client_id;

    private String series;

    private String number;

    private Date validDate;

}
