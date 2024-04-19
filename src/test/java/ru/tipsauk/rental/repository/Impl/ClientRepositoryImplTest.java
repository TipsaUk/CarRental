package ru.tipsauk.rental.repository.Impl;

import org.junit.jupiter.api.*;
import ru.tipsauk.rental.entity.Client;
import ru.tipsauk.rental.entity.Document;
import ru.tipsauk.rental.exception.EntityCreateException;
import ru.tipsauk.rental.exception.EntityOperationException;
import ru.tipsauk.rental.repository.BaseDatabaseTest;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Интеграционные тесты для ClientRepositoryImpl")
class ClientRepositoryImplTest extends BaseDatabaseTest {

    private static Client client1;
    private static Client client2;

    @BeforeAll
    public static void setUp() {
        baseSetUp();

        client1 = new Client(1, "Иван", "Иванов",
                new Document(1, "5555", "666666"
                        , Date.from(LocalDate.of(2025, 4, 14)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant())));
        client2 = new Client(2, "Петр", "Петров",
                new Document(2, "6666", "777777"
                        , Date.from(LocalDate.of(2024, 4, 14)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant())));

    }

    @BeforeEach
    void prepareTestData() throws SQLException {
        databaseService.startTransaction();
        clearTable("client");
        createClient(client1);
        createClient(client2);
        databaseService.commitTransaction();
    }

    @AfterAll
    public static void tearDown() {
        baseTearDown();
    }

    @Test
    @DisplayName("Создание клиента и документа")
    void create() {
        Client client = new Client(3, "Алексей", "Сидоров",
                new Document(3, "9999", "111111"
                        , Date.from(LocalDate.of(2027, 4, 14)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant())));
        Client newClient = clientRepository.create(client);
        documentRepository.create(newClient.getDocument());
        assertThat(clientRepository.findById(newClient.getId())).isEqualTo(newClient);
    }

    @Test
    @DisplayName("Проверка создания дубликата клиента")
    void createDuplicate() {
        assertThrows(EntityCreateException.class, () -> {
            clientRepository.create(client1);
        });

    }

    @Test
    @DisplayName("Обновление данных клиента")
    void update() {
        List<Client> clients = clientRepository.findAll();
        Client client = clients.get(0);
        client.setSurname("Кузнецов");
        clientRepository.update(client);
        Client updatedClient = clientRepository.findById(client.getId());
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getSurname()).isEqualTo("Кузнецов");
    }

    @Test
    @DisplayName("Обновление данных клиента по несуществующему id")
    void failUpdate() {
        client1.setId(99999);
        assertThrows(EntityOperationException.class, () -> {
            clientRepository.update(client1);
        });
    }

    @Test
    @DisplayName("Получение данных всех клиентов")
    void findAll() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).isNotNull().hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Получение данных клиента по id")
    void findById() {
        List<Client> clients = clientRepository.findAll();
        Client client = clientRepository.findById(clients.get(0).getId());
        assertThat(client.getName()).isEqualTo(clients.get(0).getName());
    }

    @Test
    @DisplayName("Получение данных клиента по несуществующему id")
    void failFindById() {
        assertThrows(EntityOperationException.class, () -> {
            clientRepository.findById(99999);
        });
    }

    @Test
    @DisplayName("Удаление клиента по id")
    void deleteById() {
        List<Client> clients = clientRepository.findAll();
        clientRepository.deleteById(clients.get(0).getId());
        assertThrows(EntityOperationException.class, () -> {
            clientRepository.findById(clients.get(0).getId());
        });
    }

    @Test
    @DisplayName("Удаление клиента по несуществующему id")
    void failDeleteById() {
        assertThrows(EntityOperationException.class, () -> {
            clientRepository.deleteById(99999);
        });
    }
}