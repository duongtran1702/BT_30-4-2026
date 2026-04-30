package atmin.controller;

import atmin.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @GetMapping("/")
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeRepository.findAllWithDepartment());

        return "employee-list";
    }
}