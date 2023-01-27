package com.employeedir.demo.dto;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.employeedir.demo.validation.FieldMatch;


@FieldMatch.List({
	@FieldMatch(first = "password", second = "matchingPassword", message = "Passwords must match")
})
public class CrmUser {

	
	@NotEmpty
	@Size(min = 3, message = "Must contain at least 3 characters")
	private String firstName;
	
	@NotEmpty
	@Size(min = 3, message = "Must contain at least 3 characters")
	private String lastName;
	
	@NotEmpty
	@Size(min = 3, message = "Must contain at least 3 characters")
	private String userName;
	
	@NotEmpty
	@Size(min = 3, message = "Must contain at least 3 characters")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Must contain uppercase, symbol and number")
	private String password;
	
	@NotEmpty
	@Size(min = 3, message = "Must contain at least 3 characters")
	private String matchingPassword;
	
	@NotEmpty
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Example: test@domain.com")
	private String email;
	
	private String formRole;
	
	public CrmUser() {
		
	}

	public CrmUser(String firstName, String lastName, String password, String matchingPassword,
			String formRole) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.matchingPassword = matchingPassword;
		this.formRole = formRole;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFormRole() {
		return formRole;
	}

	public void setFormRole(String formRole) {
		this.formRole = formRole;
	}
	
	
	
}
