package com.employeedir.demo.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.employeedir.demo.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	@Query(value = "SELECT e FROM Employee e "
			+ "WHERE e.firstName LIKE %?1% "
			+ "OR e.lastName LIKE %?1% "
			+ "OR e.email LIKE %?1% "
			+ "OR CONCAT(e.firstName, e.lastName) LIKE %?1% "
			+ "OR CONCAT(e.firstName, ' ', e.lastName) LIKE %?1%")
	Page<Employee> getByKeyword(String keyword, Pageable pageable);
}
