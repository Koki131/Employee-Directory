package com.employeedir.demo.service;

import java.util.List;

import com.employeedir.demo.entity.MonthlySales;
import com.employeedir.demo.entity.Sales;
import com.employeedir.demo.entity.TotalYearlySales;
import com.employeedir.demo.entity.YearlySales;


public interface SalesService {
	

    List<Sales> getSales();
    void saveSale(Sales sale);
    Sales getSale(int id);
    void deleteSale(int id);

    List<Sales> findAllSalesById(int employeeId);
    
    List<YearlySales> findAllYearlyById(int employeeId);
    
    List<MonthlySales> findAllMonthlyById(int employeeId);
    
    List<YearlySales> findAllYearly();
    
    List<TotalYearlySales> findAll();

}