package com.employeedir.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.employeedir.demo.entity.Employee;
import com.employeedir.demo.service.EmployeeService;

@Controller
public class EmployeeController {

	
	@Autowired
	private EmployeeService employeeService;
	
	
	@GetMapping("/")
	public String list(Model model) {
		
		List<Employee> employees = employeeService.findAll();
		
		model.addAttribute("employees", employees);
		
		return "/employees/employee-list";
		
	}
}
