package pl.mdj.rejestrbiurowy.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mdj.rejestrbiurowy.entity.Trip;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query(value = "select s from Trip s where function('DATE_TRUNC','day',s.date)=?1")
    List<Trip> findAllByDate(LocalDate startDateTime);

    @EntityGraph(value = "Trip")
}
