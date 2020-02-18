package pl.mdj.rejestrbiurowy.clientaccess.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.mdj.rejestrbiurowy.service.interfaces.EmployeeService;

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
        return "employees";
    }
}