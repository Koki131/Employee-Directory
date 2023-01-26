package com.employeedir.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model model) {
		
		model.addAttribute("employee", new Employee());
		
		return "/employees/employee-form";
	}
	
	@PostMapping("/addEmployee")
	public String saveEmployee(@ModelAttribute("employee") Employee employee) {
		
		employeeService.save(employee);
		
		return "redirect:/";
		
	}
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("employeeId") int employeeId, Model model) {
		
		Employee employee = employeeService.getEmployee(employeeId);
		
		model.addAttribute("employee", employee);
		
		return "/employees/employee-form";
		
	}
	
	@GetMapping("/delete") 
	public String deleteEmployee(@RequestParam("employeeId") int employeeId) {
		
		employeeService.delete(employeeId);
		
		return "redirect:/";
	}
}
