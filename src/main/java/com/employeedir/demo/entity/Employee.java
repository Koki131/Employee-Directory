package com.employeedir.demo.entity;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.annotations.Type;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
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

	@Lob
	@Type(type="org.hibernate.type.BinaryType")
	@Column(name = "image_data", columnDefinition="bytea")
	private byte[] imageData;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "employee")
	private List<Prospects> prospects;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "employee")
	private List<Sales> sales;
	
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

	public String generateBase64Image() {
		return Base64.encodeBase64String(this.imageData);
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

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public String getImagePath() {
		
		if (image == null) return null;
		
		return "/profile-images/" + id + "/" + image;
			 
	}
	
	public boolean isDir() {
		
		String dir = "./profile-images/" + this.getId();
		
		Path uploadPath = Paths.get(dir);
		
		return Files.isDirectory(uploadPath);
	}

	public List<Prospects> getProspects() {
		return prospects;
	}

	public void setProspects(List<Prospects> prospects) {
		this.prospects = prospects;
	}

	public List<Sales> getSales() {
		return sales;
	}

	public void setSales(List<Sales> sales) {
		this.sales = sales;
	}
	
	public void addSale(Sales sale) {
		if (sales == null) {
			sales = new ArrayList<>();
		}
		
		sales.add(sale);
	}
	
	public void addProspect(Prospects prospect) {
		
		if (prospects == null) {
			prospects = new ArrayList<>();
		}
		
		prospects.add(prospect);
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", image='" + image + '\'' +
				", prospects=" + prospects +
				", sales=" + sales +
				'}';
	}
}
