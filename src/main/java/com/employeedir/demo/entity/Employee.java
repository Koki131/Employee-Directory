package com.employeedir.demo.entity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@NotNull(message = "First name cannot be empty")
	@Column(name = "first_name")
	private String firstName;
	
	@NotNull(message = "Last name cannot be empty")
	@Column(name = "last_name")
	private String lastName;
	
	@NotNull(message = "E-mail cannot be empty")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Example: test@domain.com")
	@Column(name = "email")
	private String email;
	
	@Column(name = "image")
	private String image;
	
	public Employee() {
		
	}
	
	public Employee(int id) {
		this.id = id;
	}

	public Employee(String firstName, String lastName, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getImagePath() {
		
		if (image == null) return null;
		
		return "/employee-images/" + id + "/" + image;	
			 
	}
	
	public boolean isDir() {
		
		String dir = "./employee-images/" + this.getId();
		
		Path uploadPath = Paths.get(dir);
		
		return Files.isDirectory(uploadPath);
	}
	
}
