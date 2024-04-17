package ru.tipsauk.rental.service.Impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.tipsauk.rental.dto.ClientDto;
import ru.tipsauk.rental.dto.DocumentDto;
import ru.tipsauk.rental.entity.Client;
import ru.tipsauk.rental.entity.Document;
import ru.tipsauk.rental.mapper.ClientMapper;
import ru.tipsauk.rental.repository.ClientRepository;
import ru.tipsauk.rental.service.DocumentService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Тесты для ClientServiceImpl")
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client1;

    private Client client2;

    private ClientDto clientDto1;

    private ClientDto clientDto2;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        client1 = new Client(1, "Иван", "Иванов",
                new Document(1, "5555", "666666", new Date()));
        client2 = new Client(2, "Петр", "Петров",
                new Document(2, "6666", "777777", new Date()));
        clientDto1 = new ClientDto(1, "Иван", "Иванов",
                new DocumentDto(1, "5555", "666666", new Date()));
        clientDto2 = new ClientDto(2, "Петр", "Петров",
                new DocumentDto(1, "6666", "777777", new Date()));
    }

    @Test
    @DisplayName("Получение списка всех клиентов")
    void findAll() {
        List<Client> cars = Arrays.asList(client1, client2);
        when(clientRepository.findAll()).thenReturn(cars);
        when(clientMapper.clientToClientDto(client1)).thenReturn(clientDto1);
        when(clientMapper.clientToClientDto(client2)).thenReturn(clientDto2);
        List<ClientDto> result = clientService.findAll();
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(clientDto1);
        assertThat(result.get(1)).isEqualTo(clientDto2);
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Получение клиента по id")
    void findById() {
        when(clientRepository.findById(1)).thenReturn(client1);
        when(clientMapper.clientToClientDto(client1)).thenReturn(clientDto1);
        ClientDto result = clientService.findById(1);
        assertThat(result).isNotNull().isEqualTo(clientDto1);
        verify(clientRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Создание клиента")
    void create() {
        when(clientRepository.create(client1)).thenReturn(client1);
        when(clientMapper.clientDtoToClient(clientDto1)).thenReturn(client1);
        Client result = clientService.create(clientDto1);
        assertThat(result).isNotNull().isEqualTo(client1);
        verify(clientRepository, times(1)).create(client1);
    }

    @Test
    @DisplayName("Обновление клиента")
    void update() {
        when(clientRepository.update(client1)).thenReturn(client1);
        when(clientMapper.clientDtoToClient(clientDto1)).thenReturn(client1);
        Client result = clientService.update(clientDto1);
        assertThat(result).isNotNull().isEqualTo(client1);
        verify(clientRepository, times(1)).update(client1);
    }

    @Test
    @DisplayName("Удаление клиента")
    void deleteById() {
        clientService.deleteById(1);
        verify(clientRepository, times(1)).deleteById(1);
    }
}