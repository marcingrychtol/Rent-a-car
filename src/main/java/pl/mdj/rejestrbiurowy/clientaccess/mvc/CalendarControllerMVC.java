package pl.mdj.rejestrbiurowy.clientaccess.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.mdj.rejestrbiurowy.clientaccess.mvc.interfaces.BasicController;
import pl.mdj.rejestrbiurowy.model.dto.CarDto;
import pl.mdj.rejestrbiurowy.model.dto.TripDto;
import pl.mdj.rejestrbiurowy.model.entity.Trip;
import pl.mdj.rejestrbiurowy.service.interfaces.CarService;
import pl.mdj.rejestrbiurowy.service.interfaces.EmployeeService;
import pl.mdj.rejestrbiurowy.service.interfaces.TripService;
import pl.mdj.rejestrbiurowy.service.mappers.DateMapper;
import pl.mdj.rejestrbiurowy.service.mappers.TripMapper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(path ="/calendar")
public class CalendarControllerMVC {

    private Logger LOG = LoggerFactory.getLogger(CalendarControllerMVC.class);

    TripService tripService;
    CarService carService;
    EmployeeService employeeService;
    DateMapper dateMapper;

    @Autowired
    public CalendarControllerMVC(TripService tripService, CarService carService, EmployeeService employeeService, DateMapper dateMapper) {
        this.tripService = tripService;
        this.carService = carService;
        this.employeeService = employeeService;
        this.dateMapper = dateMapper;
    }

    @GetMapping("")
    public String getCallendar(@ModelAttribute TripDto tripDto, Model model) {

        LocalDate requestedDate;
        if (tripDto.getStartingDate() != null) {
            LOG.info(tripDto.getStartingDate().toString());
            requestedDate = dateMapper.toLocalDate(tripDto.getStartingDate());
        } else {
            requestedDate = LocalDate.now();
        }

        LocalDate today = LocalDate.now();

        model.addAttribute("today", today);
        model.addAttribute("year", requestedDate.getYear());
        model.addAttribute("month", requestedDate.getMonthValue());
        model.addAttribute("day", requestedDate.getDayOfMonth());
        model.addAttribute("tripDto", new TripDto());
        model.addAttribute("cars", carService.getAvailable(requestedDate));
        model.addAttribute("trips", tripService.findAllByStartingDateEquals(requestedDate));
        return "calendar/calendar";
    }


    @GetMapping("/day/{id}")
    public String getDay(@PathVariable String id, Model model) {
        return "calendar/day";
    }

    @InitBinder
    public void customizeDateBinder( WebDataBinder binder )
    {
        // tell spring to set empty values as null instead of empty string.
        binder.registerCustomEditor( Date.class, new StringTrimmerEditor( true ));

        //The date format to parse or output your dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //Create a new CustomDateEditor
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        //Register it as custom editor for the Date type
        binder.registerCustomEditor(Date.class, editor);
    }


}
