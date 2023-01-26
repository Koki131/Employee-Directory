package com.employeedir.demo.dao;

import com.employeedir.demo.model.User;

public interface UserDAO {


	User findUserByName(String userName);
	
	void save(User user);
	
	User findUserByEmail(String email);
}
