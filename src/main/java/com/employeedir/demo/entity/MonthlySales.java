package com.employeedir.demo.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "monthly_sales")
public class MonthlySales implements Comparable<MonthlySales> {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "employee_id")
	private int employeeId;
	
	
	@Column(name = "day")
	private int day;
	
	@Column(name = "month")
	private int month;
	
	@Column(name = "year")
	private int year;
	
	@Column(name = "totalSales")
	private BigDecimal totalSales;
	
	public MonthlySales() {
		
	}

	public MonthlySales(int day, int month, int year, BigDecimal totalSales) {
		this.day = day;
		this.month = month;
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

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
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

	

	@Override
	public String toString() {
		return "MonthlySales [id=" + id + ", employeeId=" + employeeId + ", day=" + day + ", month=" + month + ", year="
				+ year + ", totalSales=" + totalSales + "]";
	}

	@Override
	public int compareTo(MonthlySales o) {
		return this.day - o.getDay();
	}
	
	
}
