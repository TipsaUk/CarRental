package ru.tipsauk.rental.repository.Impl;

import org.junit.jupiter.api.*;
import ru.tipsauk.rental.entity.*;
import ru.tipsauk.rental.exception.EntityOperationException;
import ru.tipsauk.rental.repository.BaseDatabaseTest;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RentalRepositoryImplTest extends BaseDatabaseTest {

    private static CarRental carRental1;
    private static CarRental carRental2;

    @BeforeAll
    public static void setUp() {
        baseSetUp();
        carRental1 = new CarRental(1, 1, 1, Date.from(LocalDate.of(2024, 4, 19)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(LocalDate.of(2024, 4, 20)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()), RentalStatus.BOOKED);
        carRental2 = new CarRental(2, 1, 2, Date.from(LocalDate.of(2025, 4, 18)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(LocalDate.of(2024, 4, 19)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()), RentalStatus.RENTED);
        Client client1 = new Client(1, "Иван", "Иванов",
                new Document(1, "5555", "666666"
                        , Date.from(LocalDate.of(2025, 4, 14)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant())));
        createClient(client1);
    }

    @BeforeEach
    void prepareTestData() throws SQLException {
        databaseService.startTransaction();
        clearTable("car_rental");
        rentalRepository.create(carRental1);
        rentalRepository.create(carRental2);
        databaseService.commitTransaction();
    }

    @AfterAll
    public static void tearDown() {
        baseTearDown();
    }

    @Test
    void create_WhenCreatingCarRental_ExpectSuccessfulCreation() {
        CarRental carRental = new CarRental(0, 1, 1, Date.from(LocalDate.of(2024, 4, 25)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(LocalDate.of(2024, 4, 25)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()), RentalStatus.BOOKED);
        CarRental newCarRental = rentalRepository.create(carRental);
        assertThat(rentalRepository.findById(newCarRental.getId())).isEqualTo(newCarRental);
    }

    @Test
    void update_WhenUpdatingCarRental_ExpectSuccessfulUpdate() {
        List<CarRental> rentals = rentalRepository.findAll();
        CarRental rental = rentals.get(0);
        rental.setStatus(RentalStatus.RENTED);

        rentalRepository.update(rental);

        CarRental updatedRental = rentalRepository.findById(rental.getId());
        assertThat(updatedRental).isNotNull();
        assertThat(updatedRental.getStatus()).isEqualTo(RentalStatus.RENTED);
    }

    @Test
    void update_WhenUpdatingCarRentalWithNonExistentId_ExpectEntityOperationException() {
        carRental1.setId(999999);
        assertThrows(EntityOperationException.class, () -> {
            rentalRepository.update(carRental1);
        });
    }

    @Test
    void findAll_WhenRetrievingAllCarRentals_ExpectCorrectNumberOfCarRentals() {
        List<CarRental> rentals = rentalRepository.findAll();
        assertThat(rentals).isNotNull().hasSize(2);
    }

    @Test
    void findById_WhenRetrievingCarRentalById_ExpectCorrectCarRental() {
        List<CarRental> rentals = rentalRepository.findAll();
        CarRental rental = rentalRepository.findById(rentals.get(0).getId());
        assertThat(rental).isEqualTo(rentals.get(0));
    }

    @Test
    void findById_WhenRetrievingCarRentalByNonExistentId_ExpectEntityOperationException() {
        assertThrows(EntityOperationException.class, () -> {
            rentalRepository.findById(999999);
        });
    }

    @Test
    void deleteById_WhenDeletingCarRentalById_ExpectSuccessfulDeletion() {
        List<CarRental> rentals = rentalRepository.findAll();
        rentalRepository.deleteById(rentals.get(0).getId());
        assertThrows(EntityOperationException.class, () -> {
            rentalRepository.deleteById(rentals.get(0).getId());
        });
    }

    @Test
    void deleteById_WhenDeletingCarRentalByNonExistentId_ExpectEntityOperationException() {
        assertThrows(EntityOperationException.class, () -> {
            rentalRepository.deleteById(99999);
        });
    }
}