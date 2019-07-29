package com.academy.onlineAcademy.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TORDER")
@NamedQueries({ @NamedQuery(name = "getAllOrders", query = "SELECT o FROM Order o"),
		@NamedQuery(name = "getAllUnpaidOrdersByUser", query = "SELECT o FROM Order o WHERE o.userId = :userId AND o.isPaid = false"),
		@NamedQuery(name = "getAllPaidOrdersByUser", query = "SELECT o FROM Order o WHERE o.userId = :userId AND o.isPaid = true"),
		@NamedQuery(name = "findOrderById", query = "SELECT o FROM Order o WHERE o.id = :id"),
		@NamedQuery(name = "orderByTotalPriceAsc", query = "SELECT o FROM Order o ORDER BY o.price ASC"),
		@NamedQuery(name = "orderByDateAsc", query = "SELECT o FROM Order o ORDER BY o.purchaseDate ASC"),
		@NamedQuery(name = "deleteByOrderId", query = "DELETE FROM Order o WHERE o.id = :id") })
public class Order implements Serializable {

	@Id
	// @GeneratedValue(strategy = GenerationType.SEQUENCE)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, updatable = false)
	private int id;

	@Column(name = "USER_ID", nullable = false)
	private int userId;

	@Column(name = "COURSE_ID", nullable = false)
	private int courseId;

	@Column(name = "PURCHASE_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date purchaseDate;

	@Column(name = "IS_PAID", nullable = false)
	private boolean isPaid = false;

	@Column(nullable = false)
	private double price;

	@JoinColumn(name = "PURCHASED_COURSES", updatable = false)
	@OneToMany(targetEntity = Course.class, mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Course> purchasedCourses;

	@ManyToOne
//	@PrimaryKeyJoinColumn(name="PERSON_ID")
	@JoinColumn(name = "PERSON_ID")
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

	/**
	 * Class constructor.
	 */
	public Order() {

	}

	/**
	 * Parameterized class constructor.
	 * 
	 * @param userId       - the id of the user that places the order
	 * @param courseId     - the id of the purchased course
	 * @param purchaseDate - the purchase date
	 * @param isPaid       - whether or not an order is complete: if true - the
	 *                     order has been completed = paid
	 * @param price        - the total price that should be paid to complete the
	 *                     order
	 */
	public Order(int userId, int courseId, Date purchaseDate, boolean isPaid, double price) {
		super();
		this.userId = userId;
		this.courseId = courseId;
		this.purchaseDate = purchaseDate;
		this.isPaid = isPaid;
		this.price = price;
	}

}
