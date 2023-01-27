package com.employeedir.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employeedir.demo.entity.TotalYearlySales;


@Repository
public interface TotalYearlySalesRepo extends JpaRepository<TotalYearlySales, Long> {

}
