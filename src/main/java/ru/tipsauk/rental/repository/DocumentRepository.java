package ru.tipsauk.rental.repository;

import ru.tipsauk.rental.entity.Client;
import ru.tipsauk.rental.entity.Document;

public interface DocumentRepository {

    Document create(Document document);

    Document update(Document document);

}
