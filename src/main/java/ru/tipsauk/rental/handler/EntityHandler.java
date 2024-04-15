package ru.tipsauk.rental.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface EntityHandler {

    void findAll(HttpServletRequest request, HttpServletResponse response);

    void findById(HttpServletRequest request, HttpServletResponse response);

    void create(HttpServletRequest request, HttpServletResponse response);

    void update(HttpServletRequest request, HttpServletResponse response);

    void deleteById(HttpServletRequest request, HttpServletResponse response);
}
