package pl.mdj.rejestrbiurowy.clientaccess.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mdj.rejestrbiurowy.exceptions.EntityConflictException;
import pl.mdj.rejestrbiurowy.exceptions.EntityNotCompleteException;
import pl.mdj.rejestrbiurowy.model.dto.EmployeeDto;
import pl.mdj.rejestrbiurowy.service.EmployeeService;

@Controller
@RequestMapping("employees")
public class EmployeeControllerMVC {

    EmployeeService employeeService;

    @Autowired
    public EmployeeControllerMVC(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("")
    public String getEmployees(Model model){
        model.addAttribute("employees", employeeService.getAll());
        return "main/employees";
    }

    @GetMapping("/edit")
    public String editEmployees(Model model){
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("newEmployee", new EmployeeDto());
        return "edit/employees-edit";
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute EmployeeDto employee){
        try {
            employeeService.addOne(employee);
        } catch (EntityNotCompleteException | EntityConflictException e) {
            e.printStackTrace();
        }
        return "redirect:/employees/edit";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable String id){
        employeeService.cancelById(Long.parseLong(id));
        return "redirect:/employees/edit";
    }
}
