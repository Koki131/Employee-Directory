package com.employeedir.demo.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.employeedir.demo.entity.Employee;
import com.employeedir.demo.service.EmployeeService;



@Controller
public class EmployeeController {

	private String imageName;
	
	
	@Autowired
	private EmployeeService employeeService;
	
	
	@GetMapping("/accessDenied")
	public String accessDenied() {
		
		return "accessDenied";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		StringTrimmerEditor trimmer = new StringTrimmerEditor(true);
		
		binder.registerCustomEditor(String.class, trimmer);
	}

	/* ---- PAGINATION, SORTING and FILTERING ----*/
	
	@GetMapping("/{pageNum}")
	public String list(Model model, @PathVariable("pageNum") int pageNum, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, 
			@RequestParam(required = false, name = "keyword") String keyword) {
		
		Page<Employee> page = employeeService.findPage(pageNum, sortField, sortDir, keyword);
		int totalPages = page.getTotalPages();
		long totalItems = page.getTotalElements();
		List<Employee> employees = page.getContent();
		
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("keyword", keyword);
		
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalItems", totalItems);
		
		model.addAttribute("employees", employees);
		
		return "/employees/employee-list";
	}
	
	@GetMapping("/")
	public String listAll(Model model) {
		String keyword = null;
		return list(model, 1, "firstName", "asc", keyword);
	}
	
	/* ---- END ---- */
	
	// a method that shows the form to add a new employee
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model model, HttpServletRequest request) {
		
		
		
		if (request.isUserInRole("ROLE_MANAGER") || request.isUserInRole("ROLE_ADMIN")) {
			
			List<Employee> employees = employeeService.findAll();
			List<String> emails = new ArrayList<>();
			
			// fetching all e-mails and sending them to the view for e-mail validation
			
			for (Employee emp : employees) {
				emails.add(emp.getEmail());
			}
		
		
			model.addAttribute("employee", new Employee());
			model.addAttribute("mails", emails);
			
			
			return "/employees/employee-form";
			
		} else {
			
			return "redirect:/accessDenied";
		}
	}
	
	// save the employee
	
	@PostMapping("/save")
	public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, 
			@RequestParam("fileImage") MultipartFile multipartFile, HttpServletRequest request, Model model) throws IOException {
		
		
		if (request.isUserInRole("ROLE_MANAGER") || request.isUserInRole("ROLE_ADMIN")) {
			
						
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			
			
			// when updating an employee, but not doing anything to the image, leave the previously saved image as the profile picture
			
			if (!fileName.isEmpty()) {
				employee.setImage(fileName);
			} else {
				employee.setImage(imageName);
			}
			
			// 
			
			if (result.hasErrors()) {
				
				return "/employees/employee-form";
			}
			
			Employee savedEmployee = employeeService.save(employee);
	
			String uploadDir = "./employee-images/" + savedEmployee.getId();
	
			Path uploadPath = Paths.get(uploadDir);
			
			
			
			if (!fileName.isEmpty()) {
				
				// checks to see if the path is a directory. If it is, it removes it's contents
				
				if (Files.isDirectory(uploadPath)) {
					FileUtils.cleanDirectory(new File(uploadDir));
				} else {
				
					// otherwise, deletes the empty file and creates a directory
					
					Files.deleteIfExists(uploadPath);
					FileUtils.forceMkdir(new File(uploadDir));
				}
	
				
				InputStream inputStream = multipartFile.getInputStream();
				Path filePath = uploadPath.resolve(fileName);
	
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			
			return "redirect:/";
			
		} else {
			
			return "redirect:/accessDenied";
		}
	}
	
	// update employee
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("employeeId") int employeeId, Model model, HttpServletRequest request) {
		
		if (request.isUserInRole("ROLE_MANAGER") || request.isUserInRole("ROLE_ADMIN")) {
		
			Employee employee = employeeService.getEmployee(employeeId);
			
			this.imageName = employee.getImage();
			
			model.addAttribute("employee", employee);
			model.addAttribute("employeeName", employee.getFirstName() + " " + employee.getLastName());
			
			return "/employees/employee-form";
		
		} else {
			
			return "redirect:/accessDenied";
		}
	}
	
	// delete employee
	
	@GetMapping("/delete")
	public String deleteEmployee(@RequestParam("employeeId") int employeeId, HttpServletRequest request, RedirectAttributes model) throws IOException {
		
		if (request.isUserInRole("ROLE_ADMIN")) {
	
			employeeService.delete(employeeId);
	
			String filePath = "/home/koki/eclipse-workspace/employee-directory-main/employee-images/" + employeeId;	
	
			Path path = Paths.get(filePath);
			
			if (Files.isDirectory(path)) {
				FileUtils.deleteDirectory(new File(filePath));
			} 
			
			model.addFlashAttribute("deleteSuccess", "Employee deleted successfully");
			
			return "redirect:/";
			
		} else {
			return "redirect:/accessDenied";
		}
		
	}
}
