package com.employeedir.demo.controller;

import com.employeedir.demo.entity.Employee;
import com.employeedir.demo.entity.MonthlySales;
import com.employeedir.demo.entity.Sales;
import com.employeedir.demo.entity.YearlySales;
import com.employeedir.demo.service.EmployeeService;
import com.employeedir.demo.service.SalesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(SalesController.class)
class SalesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalesService salesServiceMock;

    @MockBean
    private EmployeeService employeeServiceMock;

    @Test
    void testShowAddSaleFormWithAdminRole() throws Exception {


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sales/showFormForSales")
                .param("employeeId", "0")
                .with(request -> {
                   request.setUserPrincipal(() -> "admin");
                   request.addUserRole("ROLE_ADMIN");
                   return request;
                });

        Employee expectedEmployee = new Employee("John", "Smith", "test123@gmail.com");

        when(employeeServiceMock.getEmployee(0)).thenReturn(expectedEmployee);
        when(salesServiceMock.findAllYearlyById(0)).thenReturn(Collections.emptyList());
        when(salesServiceMock.findAllMonthlyById(0)).thenReturn(Collections.emptyList());
        when(salesServiceMock.findAllSalesById(0)).thenReturn(Collections.emptyList());

        Employee actualEmployee = employeeServiceMock.getEmployee(0);


        assertEquals(expectedEmployee, actualEmployee);


        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("/sales/add-sale-form"))
                .andExpect(model().attributeExists("months"))
                .andExpect(model().attributeExists("years"))
                .andExpect(model().attributeExists("yearlySales"))
                .andExpect(model().attributeExists("monthlySales"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attributeExists("sale"))
                .andExpect(model().attributeExists("dates"))
                .andExpect(model().attributeExists("monthLength"));

    }

    @Test
    void testShowAddSaleFormWithEmployeeRole() throws Exception {


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sales/showFormForSales")
                .param("employeeId", "0")
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
    void testShowAddSaleFormWithManagerRole() throws Exception {


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sales/showFormForSales")
                .param("employeeId", "0")
                .with(request -> {
                    request.setUserPrincipal(() -> "manager");
                    request.addUserRole("ROLE_MANAGER");
                    return request;
                });

        Employee expectedEmployee = new Employee("John", "Smith", "test123@gmail.com");

        when(employeeServiceMock.getEmployee(0)).thenReturn(expectedEmployee);
        when(salesServiceMock.findAllYearlyById(0)).thenReturn(Collections.emptyList());
        when(salesServiceMock.findAllMonthlyById(0)).thenReturn(Collections.emptyList());
        when(salesServiceMock.findAllSalesById(0)).thenReturn(Collections.emptyList());

        Employee actualEmployee = employeeServiceMock.getEmployee(0);


        assertEquals(expectedEmployee, actualEmployee);


        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("/sales/add-sale-form"))
                .andExpect(model().attributeExists("months"))
                .andExpect(model().attributeExists("years"))
                .andExpect(model().attributeExists("yearlySales"))
                .andExpect(model().attributeExists("monthlySales"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attributeExists("sale"))
                .andExpect(model().attributeExists("dates"))
                .andExpect(model().attributeExists("monthLength"));

    }



    @Test
    void testShowPerformanceWithAdminRole() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sales/showPerformance")
                .with(request -> {
                   request.setUserPrincipal(() -> "admin");
                   request.addUserRole("ROLE_ADMIN");
                   return request;
                });

        when(salesServiceMock.findAll()).thenReturn(Collections.emptyList());
        when(salesServiceMock.findAllYearly()).thenReturn(Collections.emptyList());


        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("/sales/performance-page"))
                .andExpect(model().attributeExists("yearlySales"))
                .andExpect(model().attributeExists("totalSales"))
                .andExpect(model().attributeExists("years"));

    }

    @Test
    void testShowPerformanceWithManagerRole() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sales/showPerformance")
                .with(request -> {
                    request.setUserPrincipal(() -> "manager");
                    request.addUserRole("ROLE_MANAGER");
                    return request;
                });

        when(salesServiceMock.findAll()).thenReturn(Collections.emptyList());
        when(salesServiceMock.findAllYearly()).thenReturn(Collections.emptyList());


        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("/sales/performance-page"))
                .andExpect(model().attributeExists("yearlySales"))
                .andExpect(model().attributeExists("totalSales"))
                .andExpect(model().attributeExists("years"));

    }

    @Test
    void testShowPerformanceWithEmployeeRole() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sales/showPerformance")
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
    void testAddSaleWithAdminRole() throws Exception {

        LocalDate date = LocalDate.of(2023, 4, 21);
        BigDecimal amount = BigDecimal.valueOf(1000);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/sales/addSale")
                .with(request -> {
                   request.setUserPrincipal(() -> "admin");
                   request.addUserRole("ROLE_ADMIN");
                   return request;
                });

        Employee employee = new Employee("John", "Smith", "test123@gmail.com");
        Sales sale = new Sales(date, "John Smith", amount);
        employee.addSale(sale);
        sale.setEmployee(employee);
        sale.setFullName(employee.getFirstName() + " " + employee.getLastName());
        salesServiceMock.saveSale(sale);

        when(employeeServiceMock.getEmployee(0)).thenReturn(employee);

        Employee actualEmployee = employeeServiceMock.getEmployee(0);

        assertEquals(employee, actualEmployee);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/sales/showFormForSales?employeeId=0"));

        verify(salesServiceMock).saveSale(sale);



    }

    @Test
    void testAddSaleWithManagerRole() throws Exception {

        LocalDate date = LocalDate.of(2023, 4, 21);
        BigDecimal amount = BigDecimal.valueOf(1000);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/sales/addSale")
                .with(request -> {
                    request.setUserPrincipal(() -> "manager");
                    request.addUserRole("ROLE_MANAGER");
                    return request;
                });

        Employee employee = new Employee("John", "Smith", "test123@gmail.com");
        Sales sale = new Sales(date, "John Smith", amount);
        employee.addSale(sale);
        sale.setEmployee(employee);
        sale.setFullName(employee.getFirstName() + " " + employee.getLastName());
        salesServiceMock.saveSale(sale);

        when(employeeServiceMock.getEmployee(0)).thenReturn(employee);

        Employee actualEmployee = employeeServiceMock.getEmployee(0);

        assertEquals(employee, actualEmployee);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/sales/showFormForSales?employeeId=0"));

        verify(salesServiceMock).saveSale(sale);

    }

    @Test
    void testAddSaleWithEmployeeRole() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/sales/addSale")
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
    void testRemoveSaleWithAdminRole() throws Exception {

        LocalDate date = LocalDate.of(2023, 4, 21);
        BigDecimal amount = BigDecimal.valueOf(1000);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sales/removeSale")
                .param("date", String.valueOf(LocalDate.of(2023, 4, 21)))
                .with(request -> {
                   request.setUserPrincipal(() -> "admin");
                   request.addUserRole("ROLE_ADMIN");
                   return request;
                });

        Sales sale = new Sales(date, "John Smith", amount);

        salesServiceMock.deleteSale(sale.getId());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/sales/showFormForSales?employeeId=0"));

        verify(salesServiceMock).deleteSale(sale.getId());

    }

    @Test
    void testRemoveSaleWithManagerRole() throws Exception {

        LocalDate date = LocalDate.of(2023, 4, 21);
        BigDecimal amount = BigDecimal.valueOf(1000);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sales/removeSale")
                .param("date", String.valueOf(LocalDate.of(2023, 4, 21)))
                .with(request -> {
                    request.setUserPrincipal(() -> "manager");
                    request.addUserRole("ROLE_MANAGER");
                    return request;
                });

        Sales sale = new Sales(date, "John Smith", amount);

        salesServiceMock.deleteSale(sale.getId());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/sales/showFormForSales?employeeId=0"));

        verify(salesServiceMock).deleteSale(sale.getId());

    }

    @Test
    void testRemoveSaleWithEmployeeRole() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sales/removeSale")
                .param("date", String.valueOf(LocalDate.of(2023, 4, 21)))
                .with(request -> {
                    request.setUserPrincipal(() -> "employee");
                    request.addUserRole("ROLE_EMPLOYEE");
                    return request;
                });



        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/accessDenied"));


    }

}