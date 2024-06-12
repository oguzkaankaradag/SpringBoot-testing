package com.example.spring_boot_testing.repository;

import com.example.spring_boot_testing.model.Employee;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    //JUnit test for save employee operation
    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnEmployeeObject() {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstname("oguz")
                .lastname("karadag")
                .email("karadagoguzkaan@gmail.com")
                .build();

        //when - action or behavior that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
        assertThat(savedEmployee.getFirstname()).isEqualTo("oguz");
        assertThat(savedEmployee.getLastname()).isEqualTo("karadag");
        assertThat(savedEmployee.getEmail()).isEqualTo("karadagoguzkaan@gmail.com");

    }
    @Test
    @DisplayName("JUnit test for save employee operation with invalid data")
    public void givenEmployeeObject_whenSaveWithInvalidData_thenThrowException() {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstname(null) // Invalid data
                .lastname("karadag")
                .email("karadagoguzkaan@gmail.com")
                .build();

        // when - action or behavior that we are going test and then - verify the output
        assertThrows(DataIntegrityViolationException.class, () -> {
            employeeRepository.save(employee);
        });
    }
    @Test
    @DisplayName("JUnit test for finding all employees")
    public void givenEmployeesList_whenFindAll_thenEmployeeList() {

        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstname("Oguz")
                .lastname("Karadag")
                .email("karadagoguzkaan@gmail.com")
                .build();
        Employee employee2 = Employee.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .build();
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        // when - action or behavior that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
        assertThat(employeeList).contains(employee1, employee2);
        // Verify the details of the first employee
        Employee retrievedEmployee1 = employeeList.get(0);
        assertThat(retrievedEmployee1.getFirstname()).isEqualTo("Oguz");
        assertThat(retrievedEmployee1.getLastname()).isEqualTo("Karadag");
        assertThat(retrievedEmployee1.getEmail()).isEqualTo("karadagoguzkaan@gmail.com");

        // Verify the details of the second employee
        Employee retrievedEmployee2 = employeeList.get(1);
        assertThat(retrievedEmployee2.getFirstname()).isEqualTo("John");
        assertThat(retrievedEmployee2.getLastname()).isEqualTo("Doe");
        assertThat(retrievedEmployee2.getEmail()).isEqualTo("john.doe@example.com");
    }
    @Test
    @DisplayName("JUnit test for finding employee by ID")
    public void givenEmployeeId_whenFindById_thenReturnEmployee() {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstname("Oguz")
                .lastname("Karadag")
                .email("karadagoguzkaan@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when - action or behavior that we are going test
        Optional<Employee> foundEmployee = employeeRepository.findById(employee.getId());

        // then - verify the output
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getId()).isEqualTo(employee.getId());
        assertThat(foundEmployee.get().getFirstname()).isEqualTo(employee.getFirstname());
        assertThat(foundEmployee.get().getLastname()).isEqualTo(employee.getLastname());
        assertThat(foundEmployee.get().getEmail()).isEqualTo(employee.getEmail());

        // Additional assertions
        // Test retrieving an employee with a non-existing ID returns an empty optional
        Optional<Employee> nonExistingEmployee = employeeRepository.findById(-1L);
        assertThat(nonExistingEmployee).isEmpty();
    }
    @Test
    @DisplayName("JUnit test for finding employee by email")
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployee() {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstname("Oguz")
                .lastname("Karadag")
                .email("karadagoguzkaan@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when - action or behavior that we are going to test
        Optional<Employee> foundEmployee = employeeRepository.findByEmail("karadagoguzkaan@gmail.com");

        // then - verify the output
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getId()).isEqualTo(employee.getId());
        assertThat(foundEmployee.get().getFirstname()).isEqualTo(employee.getFirstname());
        assertThat(foundEmployee.get().getLastname()).isEqualTo(employee.getLastname());
        assertThat(foundEmployee.get().getEmail()).isEqualTo(employee.getEmail());
    }
}
