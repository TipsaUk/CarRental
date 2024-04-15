package ru.tipsauk.rental.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tipsauk.rental.dto.CarDto;
import ru.tipsauk.rental.entity.Car;
import ru.tipsauk.rental.mapper.CarMapper;
import ru.tipsauk.rental.repository.CarRepository;
import ru.tipsauk.rental.service.CarService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    private final CarMapper carMapper;

    @Override
    public List<CarDto> findAll() {
        log.info("CarServiceImpl: findAll() (Start method)");
        return carRepository.findAll().stream()
                .map(carMapper::carToCarDto)
                .collect(Collectors.toList());
    }

    @Override
    public CarDto findById(long id) {
        log.info("CarServiceImpl: findById(long id), id = " + id + " (Start method)");
        return carMapper.carToCarDto(carRepository.findById(id));
    }

    @Override
    public Car create(CarDto carDto) {
        log.info("CarServiceImpl: create(CarDto carDto), " + carDto + " (Start method)");
        return carRepository.create(carMapper.carDtoToCar(carDto));
    }

    @Override
    public Car update(CarDto carDto) {
        log.info("CarServiceImpl: update(CarDto carDto), " + carDto + " (Start method)");
        return carRepository.update(carMapper.carDtoToCar(carDto));
    }

    @Override
    public void deleteById(long id) {
        log.info("CarServiceImpl: delete(long id), id = " + id + " (Start method)");
        carRepository.deleteById(id);
    }
}
