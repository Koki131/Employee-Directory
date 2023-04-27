package com.employeedir.demo.controller;

import com.employeedir.demo.entity.Employee;
import com.employeedir.demo.service.EmployeeService;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Collections;
import java.util.List;


import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeServiceMock;

    @Test
    void testAccessDenied() throws Exception {

        mockMvc.perform(get("/accessDenied"))
                .andExpect(view().name("accessDenied"));
    }


    @Test
    void testListWithNullKeyword() throws Exception {

        Page<Employee> page = new PageImpl<>(Collections.emptyList());
        when(employeeServiceMock.findPage(0, "firstName", "asc", null)).thenReturn(page);

        mockMvc.perform(get("/0?sortField=firstName&sortDir=asc"))
                .andExpect(status().isOk())
                .andExpect(view().name("/employees/employee-list"))
                .andExpect(model().attribute("pageNum", 0))
                .andExpect(model().attribute("sortField", "firstName"))
                .andExpect(model().attribute("sortDir", "asc"))
                .andExpect(model().attribute("reverseSortDir", "desc"))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("totalItems", 0L))
                .andExpect(model().attribute("employees", page.getContent()));

        verify(employeeServiceMock).findPage(0, "firstName", "asc", null);

    }

    @Test
    void testListWithKeyword() throws Exception {

        List<Employee> employees = List.of(new Employee("John", "Smith", "test123@gmail.com"));

        Page<Employee> page = new PageImpl<>(employees);
        when(employeeServiceMock.findPage(0, "firstName", "asc", "John")).thenReturn(page);

        mockMvc.perform(get("/0?sortField=firstName&sortDir=asc&keyword=John"))
                .andExpect(status().isOk())
                .andExpect(view().name("/employees/employee-list"))
                .andExpect(model().attribute("pageNum", 0))
                .andExpect(model().attribute("sortField", "firstName"))
                .andExpect(model().attribute("sortDir", "asc"))
                .andExpect(model().attribute("reverseSortDir", "desc"))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("totalItems", 1L))
                .andExpect(model().attribute("employees", page.getContent()));

        verify(employeeServiceMock).findPage(0, "firstName", "asc", "John");

    }

    @Test
    void testShowFormForAddWithEmployeeRole() throws Exception {

        List<Employee> employees = List.of(new Employee("John", "Doe", "test123@gmail.com"));


        when(employeeServiceMock.findAll()).thenReturn(employees);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/showFormForAdd")
                .with(request -> {
                    request.setUserPrincipal(() -> "employee");
                    request.addUserRole("ROLE_EMPLOYEE");
                    return request;
                });

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/accessDenied"));
    }

    @Test
    void testShowFormForAddWithManagerRole() throws Exception {

        List<Employee> employees = List.of(new Employee("John", "Doe", "test123@gmail.com"));

        when(employeeServiceMock.findAll()).thenReturn(employees);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/showFormForAdd")
                .with(request -> {
                   request.setUserPrincipal(() -> "manager");
                   request.addUserRole("ROLE_MANAGER");
                   return request;
                });

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("/employees/employee-form"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attribute("mails", hasItem("test123@gmail.com")));
    }

    @Test
    void testShowFormForAddWithAdminRole() throws Exception {

        List<Employee> employees = List.of(new Employee("John", "Doe", "test123@gmail.com"));

        when(employeeServiceMock.findAll()).thenReturn(employees);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/showFormForAdd")
                .with(request -> {
                   request.setUserPrincipal(() -> "admin");
                   request.addUserRole("ROLE_ADMIN");
                   return request;
                });

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("/employees/employee-form"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attribute("mails", hasItem("test123@gmail.com")));

    }

    @Test
    void testSaveEmployeeWithEmployeeRole() throws Exception {

        Employee employee = new Employee("John", "Doe", "test123@gmail.com");

        MockMultipartFile file = new MockMultipartFile("fileImage", "test.jpg", "multipart/form-data", "test".getBytes());

        when(employeeServiceMock.save(any(Employee.class))).thenReturn(employee);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/save")
                .file("fileImage", file.getBytes())
                .param("firstName", employee.getFirstName())
                .param("lastName", employee.getLastName())
                .param("email", employee.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(request -> {
                    request.setUserPrincipal(() -> "employee");
                    request.addUserRole("ROLE_EMPLOYEE");
                    return request;
                });

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/accessDenied"));

    }

    @Test
    void testSaveEmployeeWithManagerRole() throws Exception {

        Employee employee = new Employee("John", "Doe", "test123@gmail.com");

        MockMultipartFile file = new MockMultipartFile("fileImage", "test.jpg", "multipart/form-data", "test".getBytes());

        when(employeeServiceMock.save(any(Employee.class))).thenReturn(employee);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/save")
                .file("fileImage", file.getBytes())
                .param("firstName", employee.getFirstName())
                .param("lastName", employee.getLastName())
                .param("email", employee.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(request -> {
                    request.setUserPrincipal(() -> "manager");
                    request.addUserRole("ROLE_MANAGER");
                    return request;
                });

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(employeeServiceMock, times(1)).save(any(Employee.class));

    }

    @Test
    void testSaveEmployeeWithAdminRole() throws Exception {

        Employee employee = new Employee("John", "Doe", "test123@gmail.com");

        MockMultipartFile file = new MockMultipartFile("fileImage", "test.jpg", "multipart/form-data", "test".getBytes());

        when(employeeServiceMock.save(any(Employee.class))).thenReturn(employee);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/save")
                .file("fileImage", file.getBytes())
                .param("firstName", employee.getFirstName())
                .param("lastName", employee.getLastName())
                .param("email", employee.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(request -> {
                    request.setUserPrincipal(() -> "admin");
                    request.addUserRole("ROLE_ADMIN");
                    return request;
                });

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(employeeServiceMock, times(1)).save(any(Employee.class));

    }

    @Test
    void testShowFormForUpdate() throws Exception {

        int employeeId = 1;

        Employee employee = new Employee("John", "Smith", "test123@gmail.com");

        when(employeeServiceMock.getEmployee(employeeId)).thenReturn(employee);

        Employee actualEmployee = employeeServiceMock.getEmployee(employeeId);

        assertEquals(employee, actualEmployee);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/showFormForUpdate")
                .param("employeeId", String.valueOf(employeeId))
                .with(request -> {
                   request.setUserPrincipal(() -> "admin");
                   request.addUserRole("ROLE_ADMIN");
                   return request;
                });

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("/employees/employee-form"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attributeExists("employeeName"))
                .andExpect(model().attribute("employee", equalTo(employee)))
                .andExpect(model().attribute("employeeName", equalTo("John Smith")));

    }

    @Test
    void testDeleteEmployeeWithEmployeeRole() throws Exception {

        int employeeId = 1;

        // Change to your working directory
        String filePath = "C:\\your\\full\\path\\employee-directory\\employee-images\\" + employeeId;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/delete")
                .param("employeeId", String.valueOf(employeeId))
                .with(request -> {
                   request.setUserPrincipal(() -> "employee");
                   request.addUserRole("ROLE_EMPLOYEE");
                   return request;
                });


        Path path = Paths.get(filePath);
        FileUtils.forceMkdir(path.toFile());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/accessDenied"));

        assertTrue(Files.exists(path));

    }

    @Test
    void testDeleteEmployeeWithManagerRole() throws Exception {

        int employeeId = 1;
        // Change to your working directory
        String filePath = "C:\\your\\full\\path\\employee-directory\\employee-images\\" + employeeId;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/delete")
                .param("employeeId", String.valueOf(employeeId))
                .with(request -> {
                    request.setUserPrincipal(() -> "manager");
                    request.addUserRole("ROLE_MANAGER");
                    return request;
                });


        Path path = Paths.get(filePath);
        FileUtils.forceMkdir(path.toFile());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/accessDenied"));

        assertTrue(Files.exists(path));

    }

    @Test
    void testDeleteEmployeeWithAdminRole() throws Exception {

        int employeeId = 1;
        String filePath = "C:\\your\\full\\path\\employee-directory\\employee-images\\" + employeeId;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/delete")
                .param("employeeId", String.valueOf(employeeId))
                .with(request -> {
                    request.setUserPrincipal(() -> "admin");
                    request.addUserRole("ROLE_ADMIN");
                    return request;
                });


        Path path = Paths.get(filePath);
        FileUtils.forceMkdir(path.toFile());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(employeeServiceMock).delete(employeeId);
        assertFalse(Files.exists(path));

    }
}