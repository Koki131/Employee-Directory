package com.employeedir.demo.service;

import com.employeedir.demo.entity.ProspectLinks;

public interface ProspectLinkService {
	
	
	void saveProspectLinks(ProspectLinks links);
	
	ProspectLinks getProspectLinks(int linkId);
	
	void deleteProspectLinks(int linkId);
}
