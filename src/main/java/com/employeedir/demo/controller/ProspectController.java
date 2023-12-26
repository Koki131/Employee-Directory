package com.employeedir.demo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.employeedir.demo.chat.model.ChatUser;
import com.employeedir.demo.model.User;
import com.employeedir.demo.securityservice.UserService;
import com.employeedir.demo.util.FetchAuthenticatedUser;
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
import com.employeedir.demo.entity.ProspectLinks;
import com.employeedir.demo.entity.Prospects;
import com.employeedir.demo.service.EmployeeService;
import com.employeedir.demo.service.ProspectLinkService;
import com.employeedir.demo.service.ProspectService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ProspectController {

	private int empId;
	
	private int prospId;
	
	@Autowired
	private ProspectService prospectService;
	
	@Autowired
	private ProspectLinkService linksService;
	
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private FetchAuthenticatedUser userUtil;
	
	@InitBinder
	public void initBinder(WebDataBinder binder, Principal principal) {



		StringTrimmerEditor trimmer = new StringTrimmerEditor(true);


		binder.registerCustomEditor(String.class, trimmer);
	}
	
	@GetMapping("/showProspectList")
	public String showProspectList(@RequestParam("employeeId") int employeeId, Model model, Principal principal) {


		model.addAttribute("currentUser", userUtil.getUser());
		
		List<Prospects> prospects = prospectService.findAllProspectsById(employeeId);
		
		empId = employeeId;
		
		model.addAttribute("prospects", prospects);
		model.addAttribute("user", principal.getName());
		
		return "/prospects/prospect-list";
	}
	
	@GetMapping("/search")
	public String searchProspects(@RequestParam(required = false, name = "keyword") String keyword, Model model, Principal principal) {
		
		List<Prospects> prospects = prospectService.findAllByKeyword(empId, keyword);
		
		model.addAttribute("prospects", prospects);
		model.addAttribute("keyword", keyword);
		model.addAttribute("user", principal.getName());
		model.addAttribute("currentUser", userUtil.getUser());
		
		if (keyword == null) {
			return "redirect:/showProspectList?employeeId=" + empId;
		}
		
		return "/prospects/prospect-list";

	}
	
	@GetMapping("/prospectAddForm")
	public String showProspectForm(Model model, Principal principal) {
		
		List<Prospects> prospects = prospectService.findAllProspectsById(empId);
		
		
		List<String> emails = new ArrayList<>();
		
		for (Prospects prosp : prospects) {
			emails.add(prosp.getEmail());
		}
		
		model.addAttribute("emails", emails);
		
		model.addAttribute("prospect", new Prospects());
		model.addAttribute("user", principal.getName());
		model.addAttribute("currentUser", userUtil.getUser());
		
		return "/prospects/prospect-form";
	}
	
	@PostMapping("/addProspect")
	public String addProspect(@Valid @ModelAttribute("prospect") Prospects prospect, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			model.addAttribute("currentUser", userUtil.getUser());
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
	public String showProspectUpdateForm(@RequestParam("prospectId") int id, Model model, Principal principal) {
		
		Prospects prospect = prospectService.getProspect(id);
		
		model.addAttribute("prospect", prospect);
		model.addAttribute("user", principal.getName());
		model.addAttribute("currentUser", userUtil.getUser());
		
		return "/prospects/prospect-form";
		
	}
	

	@GetMapping("/showProspectLinks")
	public String showProspectLinks(@RequestParam("prospectId") int id, Model model, Principal principal) {

		prospId = id;
		
		Prospects prospects = prospectService.getProspect(id);
		
		if (prospects.getProspectLinks() != null) {
			
			ProspectLinks links = linksService.getProspectLinks(prospects.getProspectLinks().getId());
			
			model.addAttribute("prospectLinks", links);
			model.addAttribute("facebook", links.getFacebook());
			model.addAttribute("instagram", links.getInstagram());
			model.addAttribute("linkedin", links.getLinkedIn());
			model.addAttribute("user", principal.getName());
			model.addAttribute("currentUser", userUtil.getUser());


			return "/prospects/prospect-links";
			
		}
		model.addAttribute("prospectLinks", new ProspectLinks());
		model.addAttribute("user", principal.getName());
		model.addAttribute("currentUser", userUtil.getUser());
		
		return "/prospects/prospect-links";
		
	}
	
	@PostMapping("/saveProspectLinks")
	public String saveProspectLinks(@Valid @ModelAttribute("prospectLinks") ProspectLinks prospectLinks, RedirectAttributes model) {
		
		Prospects prospect = prospectService.getProspect(prospId);
		
		prospectLinks.setProspects(prospect);
		
		linksService.saveProspectLinks(prospectLinks);

		model.addFlashAttribute("currentUser", userUtil.getUser());

		return "redirect:/showProspectList?employeeId=" + empId;
		
		
	}
	
}