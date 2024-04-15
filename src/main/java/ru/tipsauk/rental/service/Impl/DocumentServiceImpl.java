package ru.tipsauk.rental.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tipsauk.rental.dto.DocumentDto;
import ru.tipsauk.rental.entity.Document;
import ru.tipsauk.rental.mapper.DocumentMapper;
import ru.tipsauk.rental.repository.DocumentRepository;
import ru.tipsauk.rental.service.DocumentService;

@Slf4j
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    @Override
    public Document create(DocumentDto documentDto) {
        log.info("DocumentServiceImpl: create(DocumentDto documentDto), " + documentDto + " (Start method)");
        return documentRepository.create(documentMapper.DocumentDtoToDocument(documentDto));
    }

    @Override
    public Document update(DocumentDto documentDto) {
        log.info("DocumentServiceImpl: update(DocumentDto documentDto), " + documentDto + " (Start method)");
        return documentRepository.update(documentMapper.DocumentDtoToDocument(documentDto));
    }

}
