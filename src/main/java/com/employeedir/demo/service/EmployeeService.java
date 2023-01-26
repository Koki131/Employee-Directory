package com.employeedir.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.employeedir.demo.entity.Employee;



public interface EmployeeService {

	List<Employee> findAll();
	
	Employee getEmployee(int employeeId);
	
	Employee save(Employee employee);
	
	void delete(int employeeId);
	
	Page<Employee> findPage(int pageNum, String sortField, String sortDir, String keyword);
}
