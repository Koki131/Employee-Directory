package com.employeedir.demo.service;

import java.util.List;

import com.employeedir.demo.entity.Prospects;



public interface ProspectService {

	void saveProspect(Prospects prospect);
	
	Prospects getProspect(int prospectId);
	
	void deleteProspect(int prospectId);
	
	List<Prospects> findAllProspectsById(int employeeId);
	
	List<Prospects> findAllByKeyword(int employeeId, String keyword);
	
}