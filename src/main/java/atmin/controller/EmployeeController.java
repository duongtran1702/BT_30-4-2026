package atmin.controller;

import atmin.model.Department;
import atmin.model.Employee;
import atmin.repository.DepartmentRepository;
import atmin.repository.EmployeeRepository;
import atmin.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UploadService uploadService;

    @GetMapping("/")
    public String listEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "") String search,
            Model model) {

        if (page < 0) page = 0;

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<Employee> employeePage;
        if (search.isBlank()) {
            employeePage = employeeRepository.findAllWithDepartment(pageable);
        } else {
            employeePage = employeeRepository.findByNameContainingIgnoreCase(search, pageable);
        }

        // Validate page không quá totalPages
        int totalPages = employeePage.getTotalPages();
        if (page >= totalPages && totalPages > 0) {
            page = totalPages - 1;
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), sort));
            if (search.isBlank()) {
                employeePage = employeeRepository.findAllWithDepartment(pageable);
            } else {
                employeePage = employeeRepository.findByNameContainingIgnoreCase(search, pageable);
            }
        }

        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("totalItems", employeePage.getTotalElements());
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("search", search);

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

        if (imageUrl != null) {
            employee.setAvatar(imageUrl);
        } else if (employee.getAvatar() == null) {
            employee.setAvatar("https://via.placeholder.com/150");
        }

        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            Department dept = departmentRepository.findById(employee.getDepartment().getId()).orElse(null);
            employee.setDepartment(dept);
        }

        employeeRepository.save(employee);
        return "redirect:/";
    }
}