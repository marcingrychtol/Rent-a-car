package pl.mdj.rejestrbiurowy.clientaccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mdj.rejestrbiurowy.model.dto.BookingParamsDto;
import pl.mdj.rejestrbiurowy.model.dto.DateDto;
import pl.mdj.rejestrbiurowy.model.dto.DayDto;
import pl.mdj.rejestrbiurowy.model.dto.TripDto;
import pl.mdj.rejestrbiurowy.service.CarService;
import pl.mdj.rejestrbiurowy.service.DayService;
import pl.mdj.rejestrbiurowy.service.EmployeeService;
import pl.mdj.rejestrbiurowy.service.TripService;
import pl.mdj.rejestrbiurowy.model.DateFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Controller
@RequestMapping(path ="/calendar")
public class CalendarController {

    private Logger LOG = LoggerFactory.getLogger(CalendarController.class);

    TripService tripService;
    CarService carService;
    EmployeeService employeeService;
    DateFactory dateFactory;
    DayService dayService;

    @Autowired
    public CalendarController(TripService tripService, CarService carService, EmployeeService employeeService, DateFactory dateFactory, DayService dayService) {
        this.tripService = tripService;
        this.carService = carService;
        this.employeeService = employeeService;
        this.dateFactory = dateFactory;
        this.dayService = dayService;
    }

    @GetMapping("/day")
    public String getDay(@ModelAttribute(name = "tripDto") TripDto tripDto, @ModelAttribute DateDto dateDto, Model model) {

        LocalDate requestedDate;

        if (dateDto.getDate() != null){
            requestedDate = dateDto.getDate();
        } else if (tripDto.getStartingDate() != null){
            requestedDate = tripDto.getStartingDate();
        } else {
            requestedDate = LocalDate.now();
        }

        model.addAttribute("active", "day");
        model.addAttribute("today", dateFactory.getDateDto(LocalDate.now()));
        model.addAttribute("requestedDate", dateFactory.getDateDto(requestedDate));
        model.addAttribute("tripDto", new TripDto());
        model.addAttribute("bookingParams", new BookingParamsDto());
        model.addAttribute("dateDto", new DateDto());
        model.addAttribute("carsByDay", carService.findAllByDay(requestedDate));
        model.addAttribute("trips", tripService.findAllActiveByDate(requestedDate));
        return "main/day";
    }

    @GetMapping("/{scope}/{page}")
    public String getMonth(Model model, @PathVariable("page") String page, @PathVariable("scope") String scope) {


        model.addAttribute("page", page);
        model.addAttribute("scope", scope);
        model.addAttribute("active", "calendar");
        model.addAttribute("today", dateFactory.getDateDto(LocalDate.now()));
        model.addAttribute("calendarPreview", getDataForIndexCalendarView(Integer.parseInt(page), Integer.parseInt(scope)));
        model.addAttribute("cars", carService.findAllActive());
        model.addAttribute("tripDto", new TripDto());
        model.addAttribute("dateDto", new DateDto());
        return "main/calendar";
    }


    @GetMapping("/find")
    public String getCalendarByDay(Model model, @ModelAttribute DateDto tripDto) {
        long page = calculatePage(tripDto);
        model.addAttribute("page", page);
        model.addAttribute("scope", 14);
        model.addAttribute("active", "calendar");
        model.addAttribute("today", dateFactory.getDateDto(LocalDate.now()));
        model.addAttribute("calendarPreview", getDataForIndexCalendarView(page, 14));
        model.addAttribute("cars", carService.findAllActive());
        model.addAttribute("tripDto", new TripDto());
        model.addAttribute("dateDto", new DateDto());
        return "redirect:/calendar/14/"+page;
    }

    private long calculatePage(DateDto tripDto) {
        LocalDate now = LocalDate.now().with(DayOfWeek.MONDAY);
        long diff = DAYS.between(now, tripDto.getDate());
        long scope = 14;
        long page;
        if (diff == 0){
            page = 0;
        } else if (diff<0) {
            page = (diff+1)/scope-1;
        } else {
            page = diff/scope;
        }
        return page;
    }


    private List<DayDto> getDataForIndexCalendarView(long page, long daysByPage){
        LocalDate start = LocalDate.now().with(DayOfWeek.MONDAY).plusDays(page*daysByPage);
        LocalDate end = start.plusDays(daysByPage-1);
        return dayService.getDaysDtoBetween(start, end);
    }
}