package com.academy.onlineAcademy.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="person")
public class Person {
	
	// Private fields with annotations and data validations
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="ID", nullable = false, updatable = false)
	private int id;
	
	@Column(name="FULL_Name", nullable = false, length = 50)
	@Size(min = 3, max = 50)
	private String fullName;
	
	@Column(nullable = false, unique=true, length = 30)
	@Size(min = 3, max = 30)
	private String username;
	
	@Column(nullable = false,  unique=true, length = 50)
	@Email(message = "Email should be valid")
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	/*
	 * ^                 # start-of-string
(?=.*[0-9])       # a digit must occur at least once
(?=.*[a-z])       # a lower case letter must occur at least once
(?=.*[A-Z])       # an upper case letter must occur at least once
(?=.*[@#$%^&+=])  # a special character must occur at least once
(?=\S+$)          # no whitespace allowed in the entire string
.{8,}             # anything, at least eight places though
$                 # end-of-string
	 */
	//@Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8, 25}$")
	
	@Pattern(regexp = ".*\\.jpg|.*\\.jpeg|.*\\.gif",
	        message="Only images of type JPEG or GIF are supported.")
	@Lob
	@Column(length=100000)
	private byte[] photo;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type = Type.USER;
	
	@ManyToMany
    @JoinTable(
        name="PERSON_COURSE",
        joinColumns=
            @JoinColumn(name="PERSON_ID", referencedColumnName="ID"),
        inverseJoinColumns=
            @JoinColumn(name="COURSE_ID", referencedColumnName="ID")
    )
	@Column(name="COURSES")
	@Valid
	private List<Course> listOfCourses;
	
	@Column(name="ORDERS")
	@OneToMany(mappedBy = "userId")
	private List<Order> listOfOrders;
	
	
	// Properties
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public byte[] getPhoto() {
		return photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public List<Course> getListOfCourses() {
		return listOfCourses;
	}
	public void setListOfCourses(List<Course> listOfCourses) {
		this.listOfCourses = listOfCourses;
	}
	public List<Order> getListOfOrders() {
		return listOfOrders;
	}
	public void setListOfOrders(List<Order> listOfOrders) {
		this.listOfOrders = listOfOrders;
	}
	
	public Person() {
		
	}
	
	
	// Constructor
	public Person(int id, String fullName, String username, String email, String password, String confirmPassword, byte[] photo, Type type,
			List<Course> listOfCourses, List<Order> listOfOrders) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.photo = photo;
		this.type = type;
		this.listOfCourses = listOfCourses;
		this.listOfOrders = listOfOrders;
	}

}
