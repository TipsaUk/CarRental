package ru.tipsauk.rental.service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.tipsauk.rental.dto.CarDto;
import ru.tipsauk.rental.entity.Car;
import ru.tipsauk.rental.entity.Transmission;
import ru.tipsauk.rental.mapper.CarMapper;
import ru.tipsauk.rental.repository.CarRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car1;

    private Car car2;

    private CarDto carDto1;

    private CarDto carDto2;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        car1 = new Car(1, "Toyota", "Camry"
                , "Н145ВС175", Transmission.AUTOMATIC, "синий");
        car2 = new Car(2, "Ford", "Focus"
                ,"В745ТК175", Transmission.AUTOMATIC, "красный");
        carDto1 = new CarDto(1, "Toyota", "Camry"
                , "Н145ВС175", Transmission.AUTOMATIC, "синий");
        carDto2 = new CarDto(2, "Ford", "Focus"
                ,"В745ТК175", Transmission.AUTOMATIC, "красный");
    }

    @Test
    void findAll_WhenRetrievingAllCars_ExpectCorrectListOfCarDTOs() {
        List<Car> cars = Arrays.asList(car1, car2);
        when(carRepository.findAll()).thenReturn(cars);
        when(carMapper.carToCarDto(car1)).thenReturn(carDto1);
        when(carMapper.carToCarDto(car2)).thenReturn(carDto2);

        List<CarDto> result = carService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(carDto1);
        assertThat(result.get(1)).isEqualTo(carDto2);
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenRetrievingCarById_ExpectCorrectCarDTO() {
        when(carRepository.findById(1)).thenReturn(car1);
        when(carMapper.carToCarDto(car1)).thenReturn(carDto1);

        CarDto result = carService.findById(1);

        assertThat(result).isNotNull().isEqualTo(carDto1);
        verify(carRepository, times(1)).findById(1);
    }

    @Test
    void create_WhenCreatingCar_ExpectSuccessfulCreationAndReturnCar() {
        when(carRepository.create(car1)).thenReturn(car1);
        when(carMapper.carDtoToCar(carDto1)).thenReturn(car1);

        Car result = carService.create(carDto1);

        assertThat(result).isNotNull().isEqualTo(car1);
        verify(carRepository, times(1)).create(car1);
    }

    @Test
    void update_WhenUpdatingCar_ExpectSuccessfulUpdateAndReturnCar() {
        when(carRepository.update(car1)).thenReturn(car1);
        when(carMapper.carDtoToCar(carDto1)).thenReturn(car1);

        Car result = carService.update(carDto1);

        assertThat(result).isNotNull().isEqualTo(car1);
        verify(carRepository, times(1)).update(car1);
    }

    @Test
    void deleteById_WhenDeletingCarById_ExpectSuccessfulDeletion() {
        carService.deleteById(1);
        verify(carRepository, times(1)).deleteById(1);
    }
}