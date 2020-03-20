package pl.mdj.rejestrbiurowy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mdj.rejestrbiurowy.model.dto.TripDto;
import pl.mdj.rejestrbiurowy.model.entity.Trip;
import pl.mdj.rejestrbiurowy.repository.TripRepository;
import pl.mdj.rejestrbiurowy.service.mappers.TripMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TripServiceImpl implements TripService {


    TripRepository tripRepository;
    TripMapper tripMapper;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository, TripMapper tripMapper) {
        this.tripRepository = tripRepository;
        this.tripMapper = tripMapper;
    }

    @Override
    public List<TripDto> getAll() {
        return tripMapper.mapToDto(tripRepository.findByOrderByStartingDateAsc());
    }

    @Override
    public TripDto findById(Long id) {
        return tripMapper.mapToDto(Objects
                .requireNonNull(tripRepository
                        .findById(id)
                        .orElse(null)));
    }

    @Override
    public TripDto addOne(TripDto tripDto) {

        if (!tripDto.isComplete()){
            return null;
        }

        if (tripDto.getEndingDate() == null){
            tripDto.setEndingDate(tripDto.getStartingDate());
        }

        tripRepository.save(tripMapper.mapToEntity(tripDto));
        return tripDto;
    }

    @Override
    public void deleteById(Long id) {
        tripRepository.deleteById(id);
    }

    public List<TripDto> findAllByEmployee_Id(Long id){
        List<Trip> tripList = tripRepository.findAllByEmployee_IdOrderByStartingDateAsc(id);
        return tripMapper.mapToDto(tripList);
    }

    public List<TripDto> findAllByCar_Id(Long id){
        List<Trip> tripList = tripRepository.findAllByCar_IdOrderByStartingDateAsc(id);
        return tripMapper.mapToDto(tripList);
    }

    public List<TripDto> findAllByStartingDateEquals(LocalDate date){
        List<Trip> tripList = tripRepository.findAllByStartingDateEquals(date);
        return tripMapper.mapToDto(tripList);
    }


}
