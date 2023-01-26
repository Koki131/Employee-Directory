package com.employeedir.demo.dao;

import com.employeedir.demo.model.Role;

public interface RoleDAO {

	Role findRoleByName(String roleName);
}
