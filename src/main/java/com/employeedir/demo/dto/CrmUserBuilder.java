package com.employeedir.demo.dto;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.employeedir.demo.validation.FieldMatch;



@FieldMatch.List({
	@FieldMatch(first = "password", second = "matchingPassword", message = "Passwords must match")
})
public class CrmUserBuilder {

	@NotEmpty
	@Size(min = 3, message = "Must contain at least 3 characters")
	private String firstName;
	
	@NotEmpty
	@Size(min = 3, message = "Must contain at least 3 characters")
	private String lastName;
	
	@NotEmpty
	@Size(min = 3, message = "Must contain at least 3 characters")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Must contain uppercase, symbol and number")
	private String password;
	
	@NotEmpty
	@Size(min = 3, message = "Must contain at least 3 characters")
	private String matchingPassword;
	
	
	private String formRole;


	public CrmUserBuilder setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}


	public CrmUserBuilder setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}


	public CrmUserBuilder setPassword(String password) {
		this.password = password;
		return this;
	}


	public CrmUserBuilder setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
		return this;
	}


	public CrmUserBuilder setFormRole(String formRole) {
		this.formRole = formRole;
		return this;
	}
	
	public CrmUser getCrmUser() {
		return new CrmUser(firstName, lastName, password, matchingPassword, formRole);
	}	
	
}
