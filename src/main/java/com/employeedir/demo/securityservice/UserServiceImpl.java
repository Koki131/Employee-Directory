package com.employeedir.demo.securityservice;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.employeedir.demo.dao.RoleDAO;
import com.employeedir.demo.dao.UserDAO;
import com.employeedir.demo.dto.CrmUser;
import com.employeedir.demo.model.Role;
import com.employeedir.demo.model.User;


@Service
public class UserServiceImpl implements UserService {

	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private RoleDAO roleDAO;
		
	@Lazy
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDAO.findUserByName(username);
		
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public User findUserByName(String userName) {
		return userDAO.findUserByName(userName);
	}

	@Override
	@Transactional
	public void save(CrmUser user) {
		
		User ogUser = new User();
		
		ogUser.setFirstName(user.getFirstName());
		ogUser.setLastName(user.getLastName());
		ogUser.setUserName(user.getUserName());
		ogUser.setPassword(passwordEncoder.encode(user.getPassword()));
		ogUser.setEmail(user.getEmail());
		
		Collection<Role> roles = Arrays.asList(roleDAO.findRoleByName("ROLE_EMPLOYEE"), roleDAO.findRoleByName(user.getFormRole()));
		
		if (roleDAO.findRoleByName(user.getFormRole()) != roleDAO.findRoleByName("ROLE_EMPLOYEE")) {
			ogUser.setRoles(roles);
		} else {
			ogUser.setRoles(Arrays.asList(roleDAO.findRoleByName("ROLE_EMPLOYEE")));
		}
		
		userDAO.save(ogUser);

	}

	@Override
	@Transactional
	public User findUserByEmail(String email) {
		return userDAO.findUserByEmail(email);
	}

}
