package com.employeedir.demo.service;

import com.employeedir.demo.entity.ProspectLinks;
import com.employeedir.demo.repo.ProspectLinksRepo;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProspectLinkServiceImpl implements ProspectLinkService {

	@Autowired
	private ProspectLinksRepo repo;
	
	@Override
	@Transactional
	public void saveProspectLinks(ProspectLinks links) {
		repo.save(links);

	}

	@Override
	@Transactional
	public ProspectLinks getProspectLinks(int linkId) {
		return repo.getReferenceById(linkId);
	}

	@Override
	@Transactional
	public void deleteProspectLinks(int linkId) {
		repo.deleteById(linkId);
	}

}
