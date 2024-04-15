package ru.tipsauk.rental.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Client {

    private long id;

    private String name;

    private String surname;

    private Document document;

    public Client() {
    }

}
