package com.academy.onlineAcademy.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "TCOURSE")
@NamedQueries({ @NamedQuery(name = "getAllCourses", query = "SELECT c FROM Course c"),
//	@NamedQuery(name = "getAllCoursesByUser", query = "SELECT c FROM Course c WHERE c.userId = :userId"),
		@NamedQuery(name = "findCourseByName", query = "SELECT c FROM Course c WHERE c.name = :name"),
		@NamedQuery(name = "findCourseByContainsName", query = "SELECT c FROM Course c WHERE c.name LIKE '%:name%'"),
		@NamedQuery(name = "findCourseById", query = "SELECT c FROM Course c WHERE c.id = :id"),
		@NamedQuery(name = "findCourseByCategory", query = "SELECT c FROM Course c WHERE c.category = :category"),
		@NamedQuery(name = "findCourseByLevel", query = "SELECT c FROM Course c WHERE c.level = :level"),
		@NamedQuery(name = "findCourseByDuration", query = "SELECT c FROM Course c WHERE c.duration = :duration"),
		@NamedQuery(name = "orderByPriceAsc", query = "SELECT c FROM Course c ORDER BY c.price ASC"),
		@NamedQuery(name = "orderByPriceDesc", query = "SELECT c FROM Course c ORDER BY c.price DESC"),
		@NamedQuery(name = "deleteAllCourses", query = "DELETE FROM Course c"),
		@NamedQuery(name = "deleteByCourseId", query = "DELETE FROM Course c WHERE c.id = :id") })
public class Course implements Serializable {

	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, updatable = false)
	private int id;

	@Column(nullable = false)
	@Size(min = 3, max = 30)
	private String name;

	@Column(nullable = false)
	private String description;

	@Column(name = "TEACHER_NAME", nullable = false)
	@Size(min = 3, max = 50)
	private String teacherName;

	@Column(nullable = false)
	private int duration;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Level level;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Category category;

	@Column(nullable = false)
	private double price;

	@Column(name = "GIVES_CERTIFICATE", nullable = false)
	private boolean givesCertificate;

	@Lob
	@Column(name = "COVER_PHOTO", length = 100000)
	private byte[] coverPhoto;

	@ManyToOne
	@JoinColumn(name = "CORDER_ID")
	private Order order;

	@ManyToMany
	private List<Person> listOfPeople;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		String[] words = name.split("\\s+");
		String convertedName = new String();
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
			convertedName += words[i] + " ";
		}
		return convertedName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean getGivesCertificate() {
		return givesCertificate;
	}

	public void setGivesCertificate(boolean givesCertificate) {
		this.givesCertificate = givesCertificate;
	}

	public byte[] getCoverPhoto() {
		return coverPhoto;
	}

	public void setCoverPhoto(byte[] coverPhoto) {
		this.coverPhoto = coverPhoto;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public List<Person> getListOfPeople() {
		return listOfPeople;
	}

	public void setListOfPeople(List<Person> listOfPeople) {
		this.listOfPeople = listOfPeople;
	}

	/**
	 * Class constructor.
	 */
	public Course() {

	}

	/**
	 * Parameterized class constructor
	 * 
	 * @param name             - name of the course
	 * @param description      - course description
	 * @param teacherName      - the name of the teacher/author of the course
	 * @param duration         - duration of the course in hours
	 * @param level            - level of difficulty of the course
	 * @param category         - category of the course
	 * @param price            - price for the course
	 * @param givesCertificate - whether or not after completing the course, it
	 *                         gives a certificate
	 * @param coverPhoto       - image for the course page
	 */
	public Course(String name, String description, String teacherName, int duration, Level level, Category category,
			double price, boolean givesCertificate, byte[] coverPhoto) {
		super();
		this.name = name;
		this.description = description;
		this.teacherName = teacherName;
		this.duration = duration;
		this.level = level;
		this.category = category;
		this.price = price;
		this.givesCertificate = givesCertificate;
		this.coverPhoto = coverPhoto;
	}

}
