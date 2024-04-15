package ru.tipsauk.rental.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.tipsauk.rental.dto.CarDto;
import ru.tipsauk.rental.entity.Car;

@Mapper
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    CarDto carToCarDto(Car carRental);

    Car carDtoToCar(CarDto carRentalDto);

}
