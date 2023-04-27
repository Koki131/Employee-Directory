package com.employeedir.demo.service;

import com.employeedir.demo.entity.Employee;
import com.employeedir.demo.repo.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceImplTest {

    @Mock
    @Autowired
    private EmployeeService employeeServiceMock;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllEmployeesTest() {

        List<Employee> expectedEmployees = new ArrayList<>();
        expectedEmployees.add(new Employee("John", "Doe", "test123@hotmail.com"));
        expectedEmployees.add(new Employee("Jane", "Doe", "test123@hotmail.com"));


        when(employeeServiceMock.findAll()).thenReturn(expectedEmployees);


        List<Employee> actualEmployees = employeeServiceMock.findAll();


        verify(employeeServiceMock).findAll();
        verifyNoMoreInteractions(employeeServiceMock);
        assertEquals(expectedEmployees, actualEmployees);

    }

    @Test
    void getEmployeeByIdTest() {

        int employeeId = 1;
        Employee expectedEmployee = new Employee("John", "Doe", "test123@gmail.com");

        when(employeeServiceMock.getEmployee(employeeId)).thenReturn(expectedEmployee);

        Employee actualEmployee = employeeServiceMock.getEmployee(employeeId);

        // Assert
        verify(employeeServiceMock).getEmployee(employeeId);
        verifyNoMoreInteractions(employeeServiceMock);
        assertEquals(expectedEmployee, actualEmployee);

    }

    @Test
    void saveEmployeeTest() {

        Employee employeeToSave = new Employee("John", "Doe", "test123@gmail.com");
        Employee expectedSavedEmployee = new Employee("John", "Doe", "test123@gmail.com");

        when(employeeServiceMock.save(employeeToSave)).thenReturn(expectedSavedEmployee);

        Employee actualSavedEmployee = employeeServiceMock.save(employeeToSave);

        // Assert
        verify(employeeServiceMock).save(employeeToSave);
        verifyNoMoreInteractions(employeeServiceMock);
        assertEquals(expectedSavedEmployee, actualSavedEmployee);


    }

    @Test
    void deleteEmployeeTest() {

        int employeeId = 1;

        employeeServiceMock.delete(employeeId);

        verify(employeeServiceMock).delete(employeeId);
        verifyNoMoreInteractions(employeeServiceMock);


    }

    @Test
    void findPageWithKeywordTest() {

        int pageNum = 1;
        String sortDir = "asc";
        String sortField = "firstName";
        String keyword = "John";

        Pageable pageable = PageRequest.of(pageNum - 1, 5, Sort.by(sortField).ascending());
        List<Employee> employees = List.of(new Employee("John", "Doe", "test123@gmail.com"));

        Page<Employee> expectedPage = new PageImpl<>(employees, pageable, 5);

        when(employeeServiceMock.findPage(pageNum, sortField, sortDir, keyword)).thenReturn(expectedPage);

        Page<Employee> actualPage = employeeServiceMock.findPage(pageNum, sortField, sortDir, keyword);

        verify(employeeServiceMock).findPage(pageNum, sortField, sortDir, keyword);
        verifyNoMoreInteractions(employeeServiceMock);
        assertEquals(expectedPage, actualPage);

    }


    @Test
    void findPageWhenPageNumIsZero() {


        when(employeeServiceMock.findPage(0, "firstName", "asc", "john")).thenThrow(IllegalArgumentException.class);


        assertThrows(IllegalArgumentException.class, () -> {

            employeeServiceMock.findPage(0, "firstName", "asc", "john");

        });


    }



}
































