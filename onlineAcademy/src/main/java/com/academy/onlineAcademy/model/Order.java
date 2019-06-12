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
import javax.persistence.Table;

@Entity
@Table(name="order")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="ID", nullable = false, updatable = false)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="USER_ID", nullable = false)
	private int userId;
	
	@JoinColumn(name="COURSE_ID", nullable = false)
	private int courseId;
	
	@Column(name="PURCHASE_DATE", nullable = false)
	private Date purchaseDate;
	
	@Column(name="IS_PAID", nullable = false)
	private boolean isPaid = false;
	
	@Column(nullable = false)
	private double price;
	
	@JoinColumn
	@OneToMany(mappedBy = "order")
	private List<Course> purchasedCourses;
	
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
	public List<Course> getPurchasedCourses() {
		return purchasedCourses;
	}
	public void setPurchasedCourses(List<Course> purchasedCourses) {
		this.purchasedCourses = purchasedCourses;
	}
	
	public Order() {
		
	}
	
	public Order(int id, int userId, int courseId, Date purchaseDate, boolean isPaid, double price) {
		super();
		this.id = id;
		this.userId = userId;
		this.courseId = courseId;
		this.purchaseDate = purchaseDate;
		this.isPaid = isPaid;
		this.price = price;
	}

}
