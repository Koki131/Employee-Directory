package com.employeedir.demo.controller;


import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.employeedir.demo.chat.model.ChatUser;
import com.employeedir.demo.dto.SaleDto;
import com.employeedir.demo.model.User;
import com.employeedir.demo.securityservice.UserService;
import com.employeedir.demo.util.FetchAuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.employeedir.demo.entity.Employee;
import com.employeedir.demo.entity.MonthlySales;
import com.employeedir.demo.entity.Sales;
import com.employeedir.demo.entity.TotalYearlySales;
import com.employeedir.demo.entity.YearlySales;
import com.employeedir.demo.service.EmployeeService;
import com.employeedir.demo.service.SalesService;


@Controller
@RequestMapping("/sales")
public class SalesController {

	
	private int empId;
	
	@Autowired
	private SalesService salesService;
	
	@Autowired
	private EmployeeService employeeService;


	@Autowired
	private FetchAuthenticatedUser userUtil;
	
	@GetMapping("/showFormForSales")
    public String showAddSaleForm(@RequestParam("employeeId") int employeeId, Model model, HttpServletRequest request, Principal principal) {
		
		if (request.isUserInRole("ROLE_MANAGER") || request.isUserInRole("ROLE_ADMIN")) {

		
			Employee employee = employeeService.getEmployee(employeeId);
			
			Sales sale = new Sales();
			
			List<YearlySales> yearlySales = salesService.findAllYearlyById(employeeId);
			List<MonthlySales> monthlySales = salesService.findAllMonthlyById(employeeId);
			List<Sales> sales = salesService.findAllSalesById(employeeId);

			Map<LocalDate, List<SaleDto>> saleTransfer = new HashMap<>();



			Set<LocalDate> dates = new HashSet<>();
			Set<Integer> years = new HashSet<>();
			Set<Integer> months = new HashSet<>();

			Map<Integer, Set<Integer>> yAndM = new HashMap<>();

	
			for (Sales s : sales) {
				dates.add(s.getDate());

				if (!saleTransfer.containsKey(s.getDate())) {
					saleTransfer.put(s.getDate(), new ArrayList<>());
				}

				saleTransfer.get(s.getDate()).add(new SaleDto(s.getId(), s.getFullName(), s.getAmount(), s.getDate()));

			}
			
			for (YearlySales s : yearlySales) {

				if (!yAndM.containsKey(s.getYear())) {
					yAndM.put(s.getYear(), new HashSet<>());
					yAndM.get(s.getYear()).add(s.getMonth());
				}

				yAndM.get(s.getYear()).add(s.getMonth());
				years.add(s.getYear());
				months.add(s.getMonth());

			}

			
			Collections.sort(yearlySales);
			Collections.sort(monthlySales);
			
			model.addAttribute("months", months);
			model.addAttribute("years", years);
			model.addAttribute("sales", saleTransfer);
	        model.addAttribute("yearlySales", yearlySales);
	        model.addAttribute("monthlySales", monthlySales);
	        model.addAttribute("employee", employee.getFirstName() + " " + employee.getLastName());
	        model.addAttribute("sale", sale);
	        model.addAttribute("dates", dates);
	        model.addAttribute("yearsAndMonths", yAndM);

			model.addAttribute("currentUser", userUtil.getUser());

			model.addAttribute("user", principal.getName());
	        
	        empId = employeeId;
	        
	        return "/sales/add-sale-form";
        
		} else {
			
			return "redirect:/accessDenied";
		}
    }
	
	@GetMapping("/showPerformance")
	public String showPerformance(Model model, HttpServletRequest request, Principal principal) {
		
		if (request.isUserInRole("ROLE_MANAGER") || request.isUserInRole("ROLE_ADMIN")) {

			List<TotalYearlySales> totalSales = salesService.findAll();
			List<YearlySales> yearlySales = salesService.findAllYearly();
			Set<Integer> years = new HashSet<>();
			
			
			for (TotalYearlySales sale : totalSales) {
				years.add(sale.getYear());
				
			}
		
			Collections.sort(yearlySales);
			
			model.addAttribute("yearlySales", yearlySales);
			model.addAttribute("totalSales", totalSales);
			model.addAttribute("years", years);
			model.addAttribute("user", principal.getName());

			model.addAttribute("currentUser", userUtil.getUser());
			
			return "/sales/performance-page";
		} else {
			
			return "redirect:/accessDenied";
		}
	}
	
	@PostMapping("/addSale")
    public String addSale(@ModelAttribute("sale") Sales sale, RedirectAttributes model, HttpServletRequest request) {
		
		if (request.isUserInRole("ROLE_MANAGER") || request.isUserInRole("ROLE_ADMIN")) {
			
			
			Employee employee = employeeService.getEmployee(empId);
	        employee.addSale(sale);
	        sale.setEmployee(employee);
	        sale.setFullName(employee.getFirstName() + " " + employee.getLastName());
	        salesService.saveSale(sale);
	        
	        model.addFlashAttribute("saleSuccess", "Sale added successfully");
	        
	        return "redirect:/sales/showFormForSales?employeeId=" + empId;
		} else {
			
			return "redirect:/accessDenied";
		}
		
   
    }
	
	@GetMapping("/removeSale")
	public String removeSale(@RequestParam("day-result") int id, HttpServletRequest request, RedirectAttributes model) {

		if (request.isUserInRole("ROLE_MANAGER") || request.isUserInRole("ROLE_ADMIN")) {


			salesService.deleteSale(id);

			model.addFlashAttribute("removeSuccess", "Sale removed successfully");
			
			return "redirect:/sales/showFormForSales?employeeId=" + empId;

		} else {
			
			return "redirect:/accessDenied";
		}
		
	}
}
