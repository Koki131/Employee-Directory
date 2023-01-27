package com.employeedir.demo.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.employeedir.demo.entity.Employee;
import com.employeedir.demo.entity.Prospects;
import com.employeedir.demo.service.EmployeeService;
import com.employeedir.demo.service.ProspectService;


@Controller
public class ProspectController {

	
	private int empId;
	
	private int prospId;
	
	@Autowired
	private ProspectService prospectService;
	
	
	@Autowired
	private EmployeeService employeeService;
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		StringTrimmerEditor trimmer = new StringTrimmerEditor(true);
		
		binder.registerCustomEditor(String.class, trimmer);
	}
	
	@GetMapping("/showProspectList")
	public String showProspectList(@RequestParam("employeeId") int employeeId, Model model) {
		
		List<Prospects> prospects = prospectService.findAllProspectsById(employeeId);
		
		empId = employeeId;
		
		model.addAttribute("prospects", prospects);
		
		return "/prospects/prospect-list";
	}
	
	@GetMapping("/search")
	public String searchProspects(@RequestParam(required = false, name = "keyword") String keyword, Model model) {
		
		List<Prospects> prospects = prospectService.findAllByKeyword(empId, keyword);
		
		model.addAttribute("prospects", prospects);
		model.addAttribute("keyword", keyword);
		
		if (keyword == null) {
			return "redirect:/showProspectList?employeeId=" + empId;
		}
		
		return "/prospects/prospect-list";
		
		
	}
	
	@GetMapping("/prospectAddForm")
	public String showProspectForm(Model model) {
		
		List<Prospects> prospects = prospectService.findAllProspectsById(empId);
		
		
		List<String> emails = new ArrayList<>();
		
		for (Prospects prosp : prospects) {
			emails.add(prosp.getEmail());
		}
		
		model.addAttribute("emails", emails);
		
		model.addAttribute("prospect", new Prospects());
		
		return "/prospects/prospect-form";
	}
	
	@PostMapping("/addProspect")
	public String addProspect(@Valid @ModelAttribute("prospect") Prospects prospect, BindingResult result) {
		
		if (result.hasErrors()) {
			
			return "/prospects/prospect-form";
		}

		
		Employee employee = employeeService.getEmployee(empId);
		
		prospect.setEmployee(employee);		
		prospectService.saveProspect(prospect);
		
		return "redirect:/showProspectList?employeeId=" + empId;
	}
	
	@GetMapping("/deleteProspect")
	public String deleteProspect(@RequestParam("prospectId") int prospectId) {
		
		prospectService.deleteProspect(prospectId);
		
		return "redirect:/showProspectList?employeeId=" + empId;
	}
	
	@GetMapping("/showProspectUpdateForm")
	public String showProspectUpdateForm(@RequestParam("prospectId") int id, Model model) {
		
		Prospects prospect = prospectService.getProspect(id);
		
		model.addAttribute("prospect", prospect);
		
		return "/prospects/prospect-form";
		
	}
	
}