package com.employeedir.demo.controller;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.employeedir.demo.entity.Employee;
import com.employeedir.demo.entity.Prospects;
import com.employeedir.demo.service.EmployeeService;
import com.employeedir.demo.service.ProspectService;


@Controller
@RequestMapping("/contact")
public class ContactController {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private ProspectService prospectService;
	
	
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/showContactForm")
	public String viewContactForm(@RequestParam("employeeId") int employeeId, Model model) {
		
		Employee employee = employeeService.getEmployee(employeeId);
		model.addAttribute("employee", employee);

		
		return "/employees/contact-form";
	}
	
	@PostMapping("/sendEmail")
	public String sendEmail(RedirectAttributes model, HttpServletRequest request,
			@ModelAttribute("employee") Employee employee, @RequestParam("attachment") MultipartFile file,
			@RequestParam("employeeId") int employeeId) throws MessagingException, UnsupportedEncodingException {
		
		String fullName = request.getParameter("fullName");
		String subject = request.getParameter("subject");
		String email = request.getParameter("email");
		String content = request.getParameter("content");
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		
		
		String mailSubject = fullName + " has sent a message";
		
		String mailContent = "<p><b>Sender Name:</b> " + fullName + "</p>";
		
		
		mailContent += "<p><b>Subject:</b> " + subject + "</p>";
		mailContent += "<p><b>Content:</b><br><br> " + content + "</p><br><br>";
		mailContent += "<hr><img style='margin-bottom: 20px; width: 20%; height: 20%;'src='cid:logo-generic' />";
		
		// Enter same e-mail as application.properties
		helper.setFrom("your-outlook-email@hotmail.com", "Big Business");
		helper.setTo(email);
		helper.setSubject(mailSubject);
		helper.setText(mailContent, true);
		
		ClassPathResource classPath = new ClassPathResource("/static/images/logo-generic.png");
		helper.addInline("logo-generic", classPath);
		
		if (!file.isEmpty()) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			
			InputStreamSource source = new InputStreamSource() {
				
				@Override
				public InputStream getInputStream() throws IOException {
					return file.getInputStream();
				}
			};
			
			helper.addAttachment(fileName, source);
		}
		
		
		
		mailSender.send(message);
		
		model.addFlashAttribute("emailSuccess", "E-mail sent successfully");
		
		return "redirect:/contact/showContactForm?employeeId=" + employeeId;
	}
	
	@GetMapping("/showProspectContactForm")
	public String showProspectContactForm(@RequestParam("prospectId") int prospectId, Model model) {
		
		Prospects prospect = prospectService.getProspect(prospectId);
		model.addAttribute("prospect", prospect);
		
		
		return "/prospects/prospect-contact-form";
		
	}
	
	@PostMapping("/sendProspectEmail")
	public String sendProspectEmail(RedirectAttributes model, HttpServletRequest request, @ModelAttribute("prospect") Prospects prospect, 
			@RequestParam("attachment") MultipartFile file, @RequestParam("prospectId") int prospectId) throws MessagingException, UnsupportedEncodingException {
	
		String fullName = request.getParameter("fullName");
		String subject = request.getParameter("subject");
		String email = request.getParameter("email");
		String content = request.getParameter("content");
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		
		
		String mailSubject = fullName + " has sent a message";
		
		String mailContent = "<p><b>Sender Name:</b> " + fullName + "</p>";
		
		
		mailContent += "<p><b>Subject:</b> " + subject + "</p>";
		mailContent += "<p><b>Content:</b><br><br> " + content + "</p><br><br>";
		mailContent += "<hr><img style='margin-bottom: 20px; width: 20%; height: 20%;'src='cid:logo-generic' />";
		
		
		// Enter same e-mail as application.properties
		helper.setFrom("your-outlook-email@hotmail.com", "Big Business");
		helper.setTo(email);
		helper.setSubject(mailSubject);
		helper.setText(mailContent, true);
		
		ClassPathResource classPath = new ClassPathResource("/static/images/logo-generic.png");
		helper.addInline("logo-generic", classPath);
		
		if (!file.isEmpty()) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			
			InputStreamSource source = new InputStreamSource() {
				
				@Override
				public InputStream getInputStream() throws IOException {
					return file.getInputStream();
				}
			};
			
			helper.addAttachment(fileName, source);
		}
		
		
		
		mailSender.send(message);
		
		model.addFlashAttribute("emailSuccess", "E-mail sent successfully");
		
		return "redirect:/contact/showProspectContactForm?prospectId=" + prospectId;
	}
}

