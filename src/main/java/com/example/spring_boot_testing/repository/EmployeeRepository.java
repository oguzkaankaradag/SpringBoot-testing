package com.example.spring_boot_testing.repository;

import com.example.spring_boot_testing.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findByLastnameLike(String lastNamePattern);
    List<Employee> findByFirstname(String firstNamePattern);

    @Query(value = "SELECT * FROM employees e WHERE e.last_name = :lastName", nativeQuery = true)
    List<Employee> findEmployeesByLastNameWithNativeQuery(@Param("lastName") String lastName);




}
