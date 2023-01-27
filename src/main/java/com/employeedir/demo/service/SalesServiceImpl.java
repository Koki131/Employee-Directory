package com.employeedir.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employeedir.demo.entity.MonthlySales;
import com.employeedir.demo.entity.Sales;
import com.employeedir.demo.entity.TotalYearlySales;
import com.employeedir.demo.entity.YearlySales;
import com.employeedir.demo.repo.MonthlySalesRepo;
import com.employeedir.demo.repo.SalesRepository;
import com.employeedir.demo.repo.TotalYearlySalesRepo;
import com.employeedir.demo.repo.YearlySalesRepo;

@Service
public class SalesServiceImpl implements SalesService {

	@Autowired
	private TotalYearlySalesRepo totalSalesRepo;
	
	@Autowired
	private YearlySalesRepo yearlySalesRepo;
	
	@Autowired
	private MonthlySalesRepo monthlySalesRepo;
	
	@Autowired
	private SalesRepository salesRepo;
	
	@Override
	public List<Sales> getSales() {
		return salesRepo.findAll();
	}

	@Override
	public void saveSale(Sales sale) {
		salesRepo.save(sale);
	}

	@Override
	public Sales getSale(int id) {
		return salesRepo.getReferenceById(id);
	}

	@Override
	public void deleteSale(int id) {
		salesRepo.deleteById(id);
	}
	
	@Override
	public List<Sales> findAllSalesById(int employeeId) {
		return salesRepo.findAllSalesById(employeeId);
	}

	@Override
	public List<YearlySales> findAllYearlyById(int employeeId) {
		return yearlySalesRepo.findSalesById(employeeId);
	}

	@Override
	public List<MonthlySales> findAllMonthlyById(int employeeId) {
		return monthlySalesRepo.findSalesById(employeeId);
	}

	@Override
	public List<TotalYearlySales> findAll() {
		return totalSalesRepo.findAll();
	}

	@Override
	public List<YearlySales> findAllYearly() {
		return yearlySalesRepo.findAll();
	}

}
