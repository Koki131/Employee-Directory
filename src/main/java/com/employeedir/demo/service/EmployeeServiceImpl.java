package com.employeedir.demo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.employeedir.demo.entity.Employee;
import com.employeedir.demo.repo.EmployeeRepository;


@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository repo;
	
	
	@Override
	@Transactional
	public List<Employee> findAll() {
		return repo.findAll();
	}

	@Override
	@Transactional
	public Employee getEmployee(int employeeId) {
		return repo.getReferenceById(employeeId);
	}

	@Override
	@Transactional
	public Employee save(Employee employee) {
		repo.save(employee);
		
		return employee;
	}

	@Override
	@Transactional
	public void delete(int employeeId) {
		repo.deleteById(employeeId);
	}
}