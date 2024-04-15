package ru.tipsauk.rental.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tipsauk.rental.dto.CarRentalDto;
import ru.tipsauk.rental.entity.CarRental;
import ru.tipsauk.rental.mapper.CarRentalMapper;
import ru.tipsauk.rental.repository.RentalRepository;
import ru.tipsauk.rental.service.RentalService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;

    private final CarRentalMapper carRentalMapper;

    @Override
    public List<CarRentalDto> findAll() {
        log.info("RentalServiceImpl: findAll() (Start method)");
        return rentalRepository.findAll().stream()
                .map(carRentalMapper::carRentalToCarRentalDto)
                .collect(Collectors.toList());
    }

    @Override
    public CarRentalDto findById(long id) {
        log.info("RentalServiceImpl: findById(long id), id = " + id + " (Start method)");
        return carRentalMapper.carRentalToCarRentalDto(rentalRepository.findById(id));
    }

    @Override
    public CarRental create(CarRentalDto carRentalDto) {
        log.info("RentalServiceImpl: create(CarRentalDto carRentalDto), " + carRentalDto + " (Start method)");
        return rentalRepository.create(carRentalMapper.carRentalDtoToCarRental(carRentalDto));
    }

    @Override
    public CarRental update(CarRentalDto carRentalDto) {
        log.info("RentalServiceImpl: update(CarRentalDto carRentalDto), " + carRentalDto + " (Start method)");
        return rentalRepository.update(carRentalMapper.carRentalDtoToCarRental(carRentalDto));
    }

    @Override
    public void deleteById(long id) {
        log.info("RentalServiceImpl: delete(long id), id = " + id + " (Start method)");
        rentalRepository.deleteById(id);
    }
}
