package pl.mdj.rejestrbiurowy.model.dto;

import lombok.Data;

import java.util.List;

/**
 * For transferring reservation parameters between
 * - booking-car-calendar
 * - CarService
 * for reservation establishment
 */

@Data
public class CarCalendarInfoDto {

//    from app
    private CarDto car;
//    into app
    private Long carId;
    private Long scope;

    private List<CarDayInfoDto> carDayList;
}
