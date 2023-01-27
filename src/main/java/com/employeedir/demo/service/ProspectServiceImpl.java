package com.employeedir.demo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employeedir.demo.entity.Prospects;
import com.employeedir.demo.repo.ProspectRepository;


@Service
public class ProspectServiceImpl implements ProspectService {

	@Autowired 
	private ProspectRepository prospectRepo;
	
	@Override
	@Transactional
	public List<Prospects> findAllProspectsById(int employeeId) {
		return prospectRepo.findAllProspectsById(employeeId);
	}

	@Override
	@Transactional
	public void saveProspect(Prospects prospect) {
		prospectRepo.save(prospect);
		
	}

	@Override
	@Transactional
	public Prospects getProspect(int prospectId) {
		return prospectRepo.getReferenceById(prospectId);
	}

	@Override
	@Transactional
	public void deleteProspect(int prospectId) {
		prospectRepo.deleteById(prospectId);
	}

	@Override
	@Transactional
	public List<Prospects> findAllByKeyword(int employeeId, String keyword) {
		
		if (keyword != null) {
			return prospectRepo.findAllByKeyword(employeeId, keyword);
		}
		
		return prospectRepo.findAllProspectsById(employeeId);
		
	}
}