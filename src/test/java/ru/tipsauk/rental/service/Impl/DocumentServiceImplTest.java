package ru.tipsauk.rental.service.Impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.tipsauk.rental.dto.DocumentDto;
import ru.tipsauk.rental.entity.Document;
import ru.tipsauk.rental.mapper.DocumentMapper;
import ru.tipsauk.rental.repository.DocumentRepository;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Тесты для DocumentServiceImplTest")
class DocumentServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentMapper documentMapper;

    @InjectMocks
    private DocumentServiceImpl documentService;

    private Document document;

    private DocumentDto documentDto;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        document = new Document(1, "5555", "666666", new Date());
        documentDto = new DocumentDto(1, "6666", "777777", new Date());
    }

    @Test
    @DisplayName("Создание документа клиента")
    void create() {
        when(documentRepository.create(document)).thenReturn(document);
        when(documentMapper.DocumentDtoToDocument(documentDto)).thenReturn(document);
        Document result = documentService.create(documentDto);
        assertThat(result).isNotNull().isEqualTo(document);
        verify(documentRepository, times(1)).create(document);
    }

    @Test
    @DisplayName("Обновление документа клиента")
    void update() {
        when(documentRepository.update(document)).thenReturn(document);
        when(documentMapper.DocumentDtoToDocument(documentDto)).thenReturn(document);
        Document result = documentService.update(documentDto);
        assertThat(result).isNotNull().isEqualTo(document);
        verify(documentRepository, times(1)).update(document);
    }
}