package com.employeedir.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.employeedir.demo.entity.YearlySales;



@Repository
public interface YearlySalesRepo extends JpaRepository<YearlySales, Integer> {

	
	@Query(value = "SELECT s FROM YearlySales s WHERE s.employeeId =:employee_id")
	List<YearlySales> findSalesById(@Param("employee_id") int employeeId);
	
}
