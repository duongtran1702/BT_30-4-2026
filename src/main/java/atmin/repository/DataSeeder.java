package atmin.repository;

import atmin.model.Department;
import atmin.model.Employee;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String @NonNull ... args) {

        if (departmentRepository.count() == 0) {

            Department itDept = new Department();
            itDept.setName("Phòng IT");
            itDept.setLocation("Tầng 4");

            Department hrDept = new Department();
            hrDept.setName("Phòng Nhân sự");
            hrDept.setLocation("Tầng 2");

            departmentRepository.saveAll(Arrays.asList(itDept, hrDept));

            Employee emp1 = new Employee();
            emp1.setName("Nguyễn Văn A");
            emp1.setAge(25);
            emp1.setStatus(1);
            emp1.setAvatar("https://i.pravatar.cc/150?u=a");
            emp1.setDepartment(itDept);

            Employee emp2 = new Employee();
            emp2.setName("Trần Thị B");
            emp2.setAge(30);
            emp2.setStatus(1);
            emp2.setAvatar("https://i.pravatar.cc/150?u=b");
            emp2.setDepartment(hrDept);

            Employee emp3 = new Employee();
            emp3.setName("Lê Văn C");
            emp3.setAge(28);
            emp3.setStatus(0);
            emp3.setAvatar("https://i.pravatar.cc/150?u=c");
            emp3.setDepartment(itDept);

            employeeRepository.saveAll(Arrays.asList(emp1, emp2, emp3));

        }
    }
}