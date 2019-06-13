package com.academy.onlineAcademy.model;



import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="TORDER")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="ID", nullable = false, updatable = false)
	private int id;
	
	@Column(name="USER_ID", nullable = false)
	private int userId;
	
	@Column(name="COURSE_ID", nullable = false)
	private int courseId;
	
	@Column(name="PURCHASE_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date purchaseDate;
	
	@Column(name="IS_PAID", nullable = false)
	private boolean isPaid = false;
	
	@Column(nullable = false)
	private double price;
	
	@JoinColumn(name="PURCHASED_COURSES", updatable=false)
	@OneToMany(targetEntity = Course.class, mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Course> purchasedCourses;
	
	@ManyToOne
	@PrimaryKeyJoinColumn(name="PERSON_ID")
	private Person person;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public boolean isPaid() {
		return isPaid;
	}
	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	//@OneToMany(target.Entity = Course.class, mappedBy = "order");
	public List<Course> getPurchasedCourses() {
		return purchasedCourses;
	}
	public void setPurchasedCourses(List<Course> purchasedCourses) {
		this.purchasedCourses = purchasedCourses;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public Order() {
		
	}
	
	public Order(/*int id, */int userId, int courseId, Date purchaseDate, boolean isPaid, double price) {
		super();
		//this.id = id;
		this.userId = userId;
		this.courseId = courseId;
		this.purchaseDate = purchaseDate;
		this.isPaid = isPaid;
		this.price = price;
	}

}
