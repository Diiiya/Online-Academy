package com.academy.onlineAcademy.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="TPERSON")
@NamedQueries({
	@NamedQuery(name = "getAllUsers", query = "SELECT p FROM Person p"),
	@NamedQuery(name = "findUserById", query = "SELECT p FROM Person p WHERE p.id = :id"),
	@NamedQuery(name = "findUserByName", query = "SELECT p FROM Person p WHERE p.fullName = :fullName"),
	@NamedQuery(name = "findUserByUsername", query = "SELECT p FROM Person p WHERE p.username = :username"),
	@NamedQuery(name = "findUserByEmail", query = "SELECT p FROM Person p WHERE p.email = :email"),
	@NamedQuery(name = "deleteAllUsers", query = "DELETE FROM Person p"),
	@NamedQuery(name = "deleteByPersonId", query = "DELETE FROM Person p WHERE p.id = :id")
})
public class Person implements Serializable{
	
	// Private fields with annotations and data validations
	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
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
	
//	@Pattern(regexp = ".*\\.jpg|.*\\.jpeg|.*\\.gif",
//	        message="Only images of type JPEG or GIF are supported.")
	@Lob
	@Column(length=100000)
	private byte[] photo = null;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type = Type.USER;
	

	@JoinColumn(name="CCOURSES", updatable=false)
	@ManyToMany
	@JoinTable(name="JOIN_PERSON_COURSE", 
	              joinColumns = { @JoinColumn(name="Id")},
	              inverseJoinColumns = { @JoinColumn(name="Id")}) 
	private List<Course> listOfCourses = null;
	
	@JoinColumn(name="CORDERS", updatable=false)
	@OneToMany(targetEntity = Order.class, mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
	private List<Order> listOfOrders = null;
	
	
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
	public Person(String fullName, String username, String email, String password, byte[] photo, Type type,
			List<Course> listOfCourses, List<Order> listOfOrders) {
		super();
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
