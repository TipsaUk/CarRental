package ru.tipsauk.rental.service.Impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.tipsauk.rental.dto.CarRentalDto;
import ru.tipsauk.rental.entity.CarRental;
import ru.tipsauk.rental.entity.RentalStatus;
import ru.tipsauk.rental.mapper.CarRentalMapper;
import ru.tipsauk.rental.repository.RentalRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Тесты для RentalServiceImplTest")
class RentalServiceImplTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CarRentalMapper carRentalMapper;

    @InjectMocks
    private RentalServiceImpl rentalService;

    private CarRental carRental1;

    private CarRental carRental2;

    private CarRentalDto carRentalDto1;

    private CarRentalDto carRentalDto2;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        carRental1 = new CarRental(1, 1, 1, new Date(), new Date(), RentalStatus.BOOKED);
        carRental2 = new CarRental(2, 2, 2, new Date(), new Date(), RentalStatus.RENTED);
        carRentalDto1 = new CarRentalDto(1, 1, 1, new Date(), new Date(), RentalStatus.BOOKED);
        carRentalDto2 = new CarRentalDto(2, 2, 2, new Date(), new Date(), RentalStatus.RENTED);
    }

    @Test
    @DisplayName("Получение списка аренды машин")
    void findAll() {
        List<CarRental> cars = Arrays.asList(carRental1, carRental2);
        when(rentalRepository.findAll()).thenReturn(cars);
        when(carRentalMapper.carRentalToCarRentalDto(carRental1)).thenReturn(carRentalDto1);
        when(carRentalMapper.carRentalToCarRentalDto(carRental2)).thenReturn(carRentalDto2);
        List<CarRentalDto> result = rentalService.findAll();
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(carRentalDto1);
        assertThat(result.get(1)).isEqualTo(carRentalDto2);
        verify(rentalRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Получение аренды машины по id")
    void findById() {
        when(rentalRepository.findById(1)).thenReturn(carRental1);
        when(carRentalMapper.carRentalToCarRentalDto(carRental1)).thenReturn(carRentalDto1);
        CarRentalDto result = rentalService.findById(1);
        assertThat(result).isNotNull().isEqualTo(carRentalDto1);
        verify(rentalRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Создание аренды машины")
    void create() {
        when(rentalRepository.create(carRental1)).thenReturn(carRental1);
        when(carRentalMapper.carRentalDtoToCarRental(carRentalDto1)).thenReturn(carRental1);
        CarRental result = rentalService.create(carRentalDto1);
        assertThat(result).isNotNull().isEqualTo(carRental1);
        verify(rentalRepository, times(1)).create(carRental1);
    }

    @Test
    @DisplayName("Обновление аренды машины")
    void update() {
        when(rentalRepository.update(carRental1)).thenReturn(carRental1);
        when(carRentalMapper.carRentalDtoToCarRental(carRentalDto1)).thenReturn(carRental1);
        CarRental result = rentalService.update(carRentalDto1);
        assertThat(result).isNotNull().isEqualTo(carRental1);
        verify(rentalRepository, times(1)).update(carRental1);
    }

    @Test
    @DisplayName("Удаление аренды машины")
    void deleteById() {
        rentalService.deleteById(1);
        verify(rentalRepository, times(1)).deleteById(1);
    }
}