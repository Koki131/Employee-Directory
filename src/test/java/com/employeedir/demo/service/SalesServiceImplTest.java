package com.employeedir.demo.service;

import com.employeedir.demo.entity.MonthlySales;
import com.employeedir.demo.entity.Sales;
import com.employeedir.demo.entity.TotalYearlySales;
import com.employeedir.demo.entity.YearlySales;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SalesServiceImplTest {


    @Mock
    @Autowired
    private SalesService salesServiceMock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSales() {

        LocalDate date = LocalDate.of(2023, 4, 23);
        BigDecimal amount = BigDecimal.valueOf(100000);

        List<Sales> expectedSales = List.of(
                new Sales(date, "John Smith",  amount),
                new Sales(date, "Jane Smith", amount)
        );

        when(salesServiceMock.getSales()).thenReturn(expectedSales);

        List<Sales> actualSales = salesServiceMock.getSales();

        verify(salesServiceMock).getSales();
        verifyNoMoreInteractions(salesServiceMock);
        assertEquals(expectedSales, actualSales);

    }

    @Test
    void saveSale() {

        LocalDate date = LocalDate.of(2023, 4, 23);
        BigDecimal amount = BigDecimal.valueOf(100000);

        Sales sale = new Sales(date, "John Smith", amount);

        salesServiceMock.saveSale(sale);

        verify(salesServiceMock).saveSale(sale);
        verifyNoMoreInteractions(salesServiceMock);

    }

    @Test
    void getSale() {

        LocalDate date = LocalDate.of(2023, 4, 23);
        BigDecimal amount = BigDecimal.valueOf(100000);

        int saleId = 1;

        Sales expectedSale = new Sales(date, "John Smith", amount);

        when(salesServiceMock.getSale(saleId)).thenReturn(expectedSale);

        Sales actualSale = salesServiceMock.getSale(saleId);

        verify(salesServiceMock).getSale(saleId);
        verifyNoMoreInteractions(salesServiceMock);
        assertEquals(expectedSale, actualSale);

    }

    @Test
    void deleteSale() {

        int saleId = 1;

        salesServiceMock.deleteSale(saleId);

        verify(salesServiceMock).deleteSale(saleId);
        verifyNoMoreInteractions(salesServiceMock);

    }

    @Test
    void findAllSalesById() {

        int employeeId = 1;
        LocalDate date = LocalDate.of(2023, 4, 23);
        BigDecimal amount = BigDecimal.valueOf(100000);

        List<Sales> expectedSales = List.of(new Sales(date, "John Smith", amount));

        when(salesServiceMock.findAllSalesById(employeeId)).thenReturn(expectedSales);

        List<Sales> actualSales = salesServiceMock.findAllSalesById(employeeId);

        verify(salesServiceMock).findAllSalesById(employeeId);
        verifyNoMoreInteractions(salesServiceMock);
        assertEquals(expectedSales, actualSales);

    }

    @Test
    void findAllYearlyById() {

        int employeeId = 1;
        BigDecimal amount = BigDecimal.valueOf(10000);

        List<YearlySales> expectedSales = List.of(new YearlySales("John Smith", 5, 2023, amount));

        when(salesServiceMock.findAllYearlyById(employeeId)).thenReturn(expectedSales);

        List<YearlySales> actualSales = salesServiceMock.findAllYearlyById(employeeId);

        verify(salesServiceMock).findAllYearlyById(employeeId);
        verifyNoMoreInteractions(salesServiceMock);
        assertEquals(expectedSales, actualSales);

    }

    @Test
    void findAllMonthlyById() {

        int employeeId = 1;
        BigDecimal amount = BigDecimal.valueOf(10000);

        List<MonthlySales> expectedSales = List.of(new MonthlySales(16, 4, 2023, amount));

        when(salesServiceMock.findAllMonthlyById(employeeId)).thenReturn(expectedSales);

        List<MonthlySales> actualSales = salesServiceMock.findAllMonthlyById(employeeId);

        verify(salesServiceMock).findAllMonthlyById(employeeId);
        verifyNoMoreInteractions(salesServiceMock);
        assertEquals(expectedSales, actualSales);


    }

    @Test
    void findAll() {

        BigDecimal amount = BigDecimal.valueOf(100000);

        List<TotalYearlySales> expectedSales = List.of(new TotalYearlySales("John Smith", 2023, amount));

        when(salesServiceMock.findAll()).thenReturn(expectedSales);

        List<TotalYearlySales> actualSales = salesServiceMock.findAll();

        verify(salesServiceMock).findAll();
        verifyNoMoreInteractions(salesServiceMock);
        assertEquals(expectedSales, actualSales);

    }

    @Test
    void findAllYearly() {

        BigDecimal amount = BigDecimal.valueOf(10000);

        List<YearlySales> expectedSales = List.of(new YearlySales("John Smith", 11, 2023, amount));

        when(salesServiceMock.findAllYearly()).thenReturn(expectedSales);

        List<YearlySales> actualSales = salesServiceMock.findAllYearly();

        verify(salesServiceMock).findAllYearly();
        verifyNoMoreInteractions(salesServiceMock);
        assertEquals(expectedSales, actualSales);

    }
}