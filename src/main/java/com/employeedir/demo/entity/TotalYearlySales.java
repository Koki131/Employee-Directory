package com.employeedir.demo.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "total_yearly_sales")
public class TotalYearlySales {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "employee_id")
	private int employeeId;
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "year")
	private int year;
	
	@Column(name = "total_sales")
	private BigDecimal totalSales;
	
	public TotalYearlySales() {
		
	}

	public TotalYearlySales(String fullName, int year, BigDecimal totalSales) {
		this.fullName = fullName;
		this.year = year;
		this.totalSales = totalSales;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public BigDecimal getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(BigDecimal totalSales) {
		this.totalSales = totalSales;
	}

	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "TotalYearlySales [id=" + id + ", employeeId=" + employeeId + ", year=" + year + ", totalSales="
				+ totalSales + "]";
	}
	
	
}
