package com.employeedir.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.employeedir.demo.dto.CrmUser;
import com.employeedir.demo.dto.CrmUserBuilder;
import com.employeedir.demo.model.User;
import com.employeedir.demo.securityservice.UserService;




@Controller
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private UserService userService;
	
	
	private Map<String, String> roles;
	
	@PostConstruct
	public void construct() {
		this.roles = new HashMap<>();
		
		roles.put("ROLE_EMPLOYEE", "Employee");
		roles.put("ROLE_MANAGER", "Manager");
		roles.put("ROLE_ADMIN", "Admin");
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		
		StringTrimmerEditor trimmer = new StringTrimmerEditor(true);
		
		binder.registerCustomEditor(String.class, trimmer);
	}
	
	@GetMapping("/showRegistrationForm")
	public String showRegistrationForm(Model model) {
		
		model.addAttribute("crmUser", new CrmUser());
		model.addAttribute("roles", roles);
		
		return "registrationPage";
	}
	
	@PostMapping("/processRegistrationForm")
	public String processRegistration(@Valid @ModelAttribute("crmUser") CrmUser crmUser, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			model.addAttribute("roles", roles);
			return "registrationPage";
		}
		
		String userName = crmUser.getUserName();
		String email = crmUser.getEmail();
		
		User user = userService.findUserByName(userName);
		User userEmail = userService.findUserByEmail(email);
		
		if (user != null || userEmail != null) {
			String firstName = crmUser.getFirstName();
			String lastName = crmUser.getLastName();
			String formRole = crmUser.getFormRole();
			
			model.addAttribute("crmUser", new CrmUserBuilder().setFirstName(firstName).setLastName(lastName).
										setFormRole(formRole).getCrmUser());
			
			model.addAttribute("registrationError", "Invalid Username or email");
			model.addAttribute("roles", roles);
			
			return "registrationPage";
		}
		
		
		userService.save(crmUser);
		model.addAttribute("registrationSuccess", "Registration successful for user " + crmUser.getUserName());
		
		return "loginPage";
	}
	
}
