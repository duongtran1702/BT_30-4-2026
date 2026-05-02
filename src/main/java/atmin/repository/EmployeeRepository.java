package atmin.repository;

import atmin.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = "SELECT e FROM Employee e LEFT JOIN FETCH e.department WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))",
            countQuery = "SELECT count(e) FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Employee> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query(value = "SELECT e FROM Employee e LEFT JOIN FETCH e.department",
            countQuery = "SELECT count(e) FROM Employee e")
    Page<Employee> findAllWithDepartment(Pageable pageable);
}