package com.employeedir.demo.securityservice;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.employeedir.demo.dto.CrmUser;
import com.employeedir.demo.model.User;



public interface UserService extends UserDetailsService {

	User findUserByName(String userName);
	
	void save(CrmUser user);
	
	User findUserByEmail(String email);
}
