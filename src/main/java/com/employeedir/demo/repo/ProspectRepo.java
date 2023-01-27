package com.employeedir.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.employeedir.demo.entity.Prospects;


@Repository
public interface ProspectRepo extends JpaRepository<Prospects, Integer> {

	@Query(value = "SELECT p FROM Prospects p WHERE p.employee.id=:employee_id")
	List<Prospects> findAllProspectsById(@Param("employee_id") int employeeId);
	
	@Query(value = "SELECT p FROM Prospects p WHERE p.employee.id=:employee_id "
			+ "AND p.fullName LIKE %:keyword% OR p.employee.id=:employee_id AND p.email LIKE %:keyword%")
	List<Prospects> findAllByKeyword(@Param("employee_id") int employeeId, @Param("keyword") String keyword);
	
}
