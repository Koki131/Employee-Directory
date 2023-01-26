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

	@Override
	@Transactional
	public Page<Employee> findPage(int pageNum, String sortField, String sortDir, String keyword) {
		
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
		
		Pageable pageable = null;
		
		if (pageNum > 0) {
			pageable = PageRequest.of(pageNum - 1, 5, sort);
		}
		
		if (keyword != null) {
			return repo.getByKeyword(keyword, pageable);
		}
		
		return repo.findAll(pageable);
		
	}
}