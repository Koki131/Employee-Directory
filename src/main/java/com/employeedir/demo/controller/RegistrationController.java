package com.employeedir.demo.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.employeedir.demo.dto.CrmUser;
import com.employeedir.demo.dto.CrmUserBuilder;
import com.employeedir.demo.model.User;
import com.employeedir.demo.securityservice.UserService;
import org.springframework.web.multipart.MultipartFile;


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
	public String processRegistration(@Valid @ModelAttribute("crmUser") CrmUser crmUser, @RequestParam("fileImage") MultipartFile multipartFile, BindingResult result, Model model) throws IOException {

		if (result.hasErrors()) {
			model.addAttribute("roles", roles);
			return "registrationPage";
		}

		String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

		if (!fileName.isEmpty()) {
			crmUser.setImage(fileName);
		}

		String uploadDir = "./profile-images/" + crmUser.getUserName();

		Path uploadPath = Paths.get(uploadDir);

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
