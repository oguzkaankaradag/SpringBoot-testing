package com.example.spring_boot_testing.integration;

import com.example.spring_boot_testing.model.Employee;
import com.example.spring_boot_testing.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc; //to perform http requests

    @Autowired
    private EmployeeRepository employeeRepository; //to keep db clean

    @Autowired
    private ObjectMapper objectMapper; //for serialization and deserialization

    @BeforeEach
    public void setup() {
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("JUnit I test case for create employee REST API")
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstname("Oguz")
                .lastname("KARADAG")
                .email("karadagoguzkaan@gmail.com")
                .build();

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result or output using assert statements
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", // Corrected JSON path
                        is(employee.getFirstname()))) // Adjusted field name
                .andExpect(jsonPath("$.lastname", // Corrected JSON path
                        is(employee.getLastname()))) // Adjusted field name
                .andExpect(jsonPath("$.email", // Corrected JSON path
                        is(employee.getEmail()))); // Adjusted field name
    }

    @Test
    @DisplayName("JUnit I test for Get All employees REST API")
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception{
        // given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstname("Ramesh").lastname("Fadatare").email("ramesh@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstname("Tony").lastname("Stark").email("tony@gmail.com").build());
        employeeRepository.saveAll(listOfEmployees);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfEmployees.size())));

    }

    // positive scenario - valid employee id
    @Test
    @DisplayName("JUnit I test for GET employee by id REST API")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception{
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstname("Oguz")
                .lastname("KARADAG")
                .email("karadagoguzkaan@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstname", is(employee.getFirstname())))
                .andExpect(jsonPath("$.lastname", is(employee.getLastname())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    // negative scenario - valid employee id
    @Test
    @DisplayName("JUnit test for GET employee by id REST API")
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception{
        // given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstname("Oguz")
                .lastname("KARADAG")
                .email("ramesh@gmail.com")
                .build();


        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("JUnit I test for update employee REST API - positive scenario")
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception{
        // given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstname("Oguz")
                .lastname("KARADAG")
                .email("karadagoguzkaan@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstname("Selcuk")
                .lastname("KARADAG")
                .email("karadagselcuk@gmail.com")
                .build();

        employeeRepository.save(savedEmployee);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstname", is(updatedEmployee.getFirstname())))
                .andExpect(jsonPath("$.lastname", is(updatedEmployee.getLastname())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

}
