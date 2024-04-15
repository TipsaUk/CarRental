package ru.tipsauk.rental.service;

import ru.tipsauk.rental.dto.ClientDto;
import ru.tipsauk.rental.dto.DocumentDto;
import ru.tipsauk.rental.entity.Document;

public interface DocumentService {

    Document create(DocumentDto documentDto);

    Document update(DocumentDto documentDto);

}
