package com.employeedir.demo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.employeedir.demo.util.FetchAuthenticatedUser;
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
	private FetchAuthenticatedUser userUtil;

	@Autowired
	private EmployeeService employeeService;



	@InitBinder
	public void initBinder(WebDataBinder binder) {


		StringTrimmerEditor trimmer = new StringTrimmerEditor(true);

		binder.registerCustomEditor(String.class, trimmer);
	}




	@GetMapping("/accessDenied")
	public String accessDenied() {

		return "accessDenied";
	}


	/* ---- PAGINATION, SORTING and FILTERING ----*/

	@GetMapping("/{pageNum}")
	public String list(Model model, @PathVariable("pageNum") int pageNum, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir,
					   @RequestParam(required = false, name = "keyword") String keyword, Principal principal) {



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

		model.addAttribute("user", principal.getName());


		model.addAttribute("currentUser", userUtil.getUser());

		return "/employees/employee-list";
	}

	@GetMapping("/")
	public String listAll(Model model, Principal principal) {
		String keyword = null;



		return list(model, 1, "firstName", "asc", keyword, principal);
	}

	/* ---- END ---- */


	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model model, HttpServletRequest request, Principal principal) {


		if (request.isUserInRole("ROLE_MANAGER") || request.isUserInRole("ROLE_ADMIN")) {

			List<Employee> employees = employeeService.findAll();
			List<String> emails = new ArrayList<>();

			// email verification

			for (Employee emp : employees) {
				emails.add(emp.getEmail());
			}


			model.addAttribute("employee", new Employee());
			model.addAttribute("mails", emails);
			model.addAttribute("user", principal.getName());


			model.addAttribute("currentUser", userUtil.getUser());



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


			String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));


			if (result.hasErrors()) {
				model.addAttribute("currentUser", userUtil.getUser());
				return "/employees/employee-form";
			}

			byte[] image = multipartFile.getBytes();

			if (!fileName.isEmpty()) {
				employee.setImage(fileName);
				employee.setImageData(image);
			} else {
				employee.setImage(imageName);
			}

			employeeService.save(employee);


			return "redirect:/";

		} else {

			return "redirect:/accessDenied";
		}
	}

	// update employee

	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("employeeId") int employeeId, Model model, HttpServletRequest request, Principal principal) {

		if (request.isUserInRole("ROLE_MANAGER") || request.isUserInRole("ROLE_ADMIN")) {

			Employee employee = employeeService.getEmployee(employeeId);

			this.imageName = employee.getImage();

			model.addAttribute("employee", employee);
			model.addAttribute("employeeName", employee.getFirstName() + " " + employee.getLastName());
			model.addAttribute("user", principal.getName());

			model.addAttribute("currentUser", userUtil.getUser());


			return "/employees/employee-form";

		} else {

			return "redirect:/accessDenied";
		}
	}

	// delete employee

	@GetMapping("/delete")
	public String deleteEmployee(@RequestParam("employeeId") int employeeId, HttpServletRequest request, RedirectAttributes model)  {

		if (request.isUserInRole("ROLE_ADMIN")) {

			employeeService.delete(employeeId);


			model.addFlashAttribute("deleteSuccess", "Employee deleted successfully");

			return "redirect:/";

		} else {

			return "redirect:/accessDenied";

		}

	}
}
