package ru.tipsauk.rental.repository;

import ru.tipsauk.rental.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {

    List<Client> findAll();

    Client findById(long id);

    Client create(Client client);

    Client update(Client client);

    void deleteById(long id);

}
