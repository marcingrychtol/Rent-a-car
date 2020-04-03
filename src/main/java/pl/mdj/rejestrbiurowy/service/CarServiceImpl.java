package pl.mdj.rejestrbiurowy.service;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mdj.rejestrbiurowy.exceptions.CannotFindEntityException;
import pl.mdj.rejestrbiurowy.model.dto.CarDto;
import pl.mdj.rejestrbiurowy.model.entity.Car;
import pl.mdj.rejestrbiurowy.model.entity.Day;
import pl.mdj.rejestrbiurowy.model.entity.Trip;
import pl.mdj.rejestrbiurowy.repository.CarRepository;
import pl.mdj.rejestrbiurowy.repository.DayRepository;
import pl.mdj.rejestrbiurowy.repository.TripRepository;
import pl.mdj.rejestrbiurowy.model.mappers.CarMapper;
import pl.mdj.rejestrbiurowy.model.mappers.DateMapper;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
@NoArgsConstructor
public class CarServiceImpl implements CarService {

    /*
     * Car
     * CarCategory
     */

    private Logger LOG = LoggerFactory.getLogger(CarServiceImpl.class);

    CarRepository carRepository;
    CarMapper carMapper;
    TripRepository tripRepository;
    DayRepository dayRepository;
    DateMapper dateMapper;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, CarMapper carMapper, TripRepository tripRepository, DateMapper dateMapper, DayRepository dayRepository) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.tripRepository = tripRepository;
        this.dateMapper = dateMapper;
        this.dayRepository = dayRepository;
    }

    @Override
    public List<CarDto> getAll() {
        return carMapper.mapToDto(carRepository.findAll());
    }

    @Override
    public CarDto findById(Long id) throws CannotFindEntityException {
        Optional<Car> carOptional = carRepository.findById(id);
        if (carOptional.isPresent()){
            return carMapper.mapToDto(carOptional.get());
        } else {
            throw new CannotFindEntityException("Cannot find car of id: " + id);
        }
    }

    @Override
    public CarDto addOne(CarDto carDto) {
        carRepository.save(carMapper.mapToEntity(carDto));  // TODO: check duplicates
        return carDto;
    }

    @Override
    public void cancelById(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public List<CarDto> getAvailableCars(LocalDate date) {

        List<CarDto> notAvailableCars = getNotAvailableCars(date);

        return carRepository.findAll().stream()
                .map(c -> carMapper.mapToDto(c))
                .filter(c -> !notAvailableCars.contains(c))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDto> getNotAvailableCars(LocalDate date) {
        Optional<Day> day = dayRepository.findById(date);

        return  day.map(
                d -> d.getTrips().stream()
                        .map(Trip::getCar)
                        .map(car -> carMapper.mapToDto(car))
                        .collect(Collectors.toList())
        ).orElseGet(ArrayList::new);

    }

}
