package com.employeedir.demo.entity;


import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "yearly_sales")
public class YearlySales implements Comparable<YearlySales>{


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(name = "employee_id")
	private int employeeId;
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "month")
	private int month;
	
	@Column(name = "year")
	private int year;
	
	@Column(name = "total_sales")
	private BigDecimal totalSales;
	
	
	
	public YearlySales() {
		
	}

	public YearlySales(String fullName, int month, int year, BigDecimal totalSales) {
		this.fullName = fullName;
		this.month = month;
		this.year = year;
		this.totalSales = totalSales;
	}

	//	public int getSaleId() {
//		return saleId;
//	}
//
//	public void setSaleId(int saleId) {
//		this.saleId = saleId;
//	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "YearlySales [id=" + id + ", employeeId=" + employeeId + ", month=" + month + ", year=" + year
				+ ", totalSales=" + totalSales + "]";
	}

	@Override
	public int compareTo(YearlySales o) {
		
		if (this.month == o.getMonth()) {
			return 0;
		} else if (this.month > o.getMonth()) {
			return 1;
		} else {
			return -1;
		}
	}
	
	
}
