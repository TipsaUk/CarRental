package ru.tipsauk.rental.repository.Impl;

import org.junit.jupiter.api.*;
import ru.tipsauk.rental.entity.Car;
import ru.tipsauk.rental.entity.Transmission;
import ru.tipsauk.rental.exception.EntityCreateException;
import ru.tipsauk.rental.exception.EntityOperationException;
import ru.tipsauk.rental.repository.BaseDatabaseTest;

import java.sql.SQLException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryImplTest extends BaseDatabaseTest {

    private static Car car1;
    private static Car car2;

    @BeforeAll
    public static void setUp() {
        baseSetUp();

        car1 = new Car(4, "Toyota", "Camry"
                , "Н145ВС175", Transmission.AUTOMATIC, "синий");
        car2 = new Car(5, "Ford", "Focus"
                ,"В745ТК175", Transmission.AUTOMATIC, "красный");
    }

    @BeforeEach
    void prepareTestData() throws SQLException {
        databaseService.startTransaction();
        clearTable("car");
        carRepository.create(car1);
        carRepository.create(car2);
        databaseService.commitTransaction();
    }

    @AfterAll
    public static void tearDown() {
        baseTearDown();
    }

    @Test
    void create_WhenCreatingCar_ExpectSuccessfulCreation() {
        Car car = new Car(0, "ЗАЗ", "ЗАЗ-965"
                , "Е836ФА175", Transmission.MECHANICAL, "голубой");
        Car newCar = carRepository.create(car);
        assertThat(carRepository.findById(newCar.getId())).isEqualTo(newCar);
    }

    @Test
    void createDuplicate_WhenCreatingDuplicateCar_ExpectEntityCreateException() {
        assertThrows(EntityCreateException.class, () -> {
            carRepository.create(car1);
        });

    }

    @Test
    void update_WhenUpdatingCarData_ExpectSuccessfulUpdate() {
        List<Car> cars = carRepository.findAll();
        Car car = cars.get(0);
        car.setColor("белый");

        carRepository.update(car);

        Car updatedCar = carRepository.findById(car.getId());
        assertThat(updatedCar).isNotNull();
        assertThat(updatedCar.getColor()).isEqualTo("белый");
    }

    @Test
    void update_WhenUpdatingCarDataWithNonExistentId_ExpectEntityOperationException() {
        car1.setId(999999);
        assertThrows(EntityOperationException.class, () -> {
            carRepository.update(car1);
        });
    }

    @Test
    void findAll_WhenRetrievingAllCars_ExpectCorrectNumberOfCars() {
        List<Car> cars = carRepository.findAll();
        assertThat(cars).isNotNull().hasSize(2);
    }

    @Test
    void findById_WhenRetrievingCarById_ExpectCorrectCar() {
        List<Car> cars = carRepository.findAll();
        Car car = carRepository.findById(cars.get(0).getId());
        assertThat(car).isEqualTo(cars.get(0));
    }

    @Test
    void findById_WhenRetrievingCarByNonExistentId_ExpectEntityOperationException() {
        assertThrows(EntityOperationException.class, () -> {
            carRepository.findById(999999);
        });
    }

    @Test
    void deleteById_WhenDeletingCarById_ExpectSuccessfulDeletion() {
        List<Car> cars = carRepository.findAll();
        carRepository.deleteById(cars.get(0).getId());
        assertThrows(EntityOperationException.class, () -> {
            carRepository.deleteById(cars.get(0).getId());
        });
    }

    @Test
    void deleteById_WhenDeletingCarByNonExistentId_ExpectEntityOperationException() {
        assertThrows(EntityOperationException.class, () -> {
            carRepository.deleteById(999999);
        });
    }
}