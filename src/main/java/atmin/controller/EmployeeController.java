package atmin.controller;

import atmin.model.Department;
import atmin.model.Employee;
import atmin.repository.DepartmentRepository;
import atmin.repository.EmployeeRepository;
import atmin.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UploadService uploadService;

    @GetMapping("/")
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeRepository.findAllWithDepartment());

        return "employee-list";
    }

    @GetMapping("/add")
    public String addEmployee(@ModelAttribute("employee") Employee employee, Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        return "form-employee";
    }

    @PostMapping("/add")
    public String saveEmployee(@ModelAttribute Employee employee,
                               @RequestParam("file") MultipartFile file) {
        String imageUrl = uploadService.uploadFile(file);
        employee.setAvatar(Objects.requireNonNullElse(imageUrl, "https://via.placeholder.com/150"));

        if (employee.getDepartment().getId() != null) {
            Department dept = departmentRepository.findById(employee.getDepartment().getId())
                    .orElse(null);
            employee.setDepartment(dept);
        }

        employeeRepository.save(employee);
        return "redirect:/";
    }
}