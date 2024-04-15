package ru.tipsauk.rental.service;

import java.util.List;

public interface EntityService<T,E> {

    List<T> findAll();

    T findById(long id);

    E create(T entity);

    E update(T entity);

    void deleteById(long id);

}
