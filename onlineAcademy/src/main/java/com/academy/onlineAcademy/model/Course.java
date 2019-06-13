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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="TCOURSE")
public class Course {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="ID", nullable = false, updatable = false)
	private int id;
	
	@Column(nullable = false)
	@Size(min = 3, max = 30)
	private String name;
	
	@Column(nullable = false)
	private String description;
	
	@Column(name="TEACHER_NAME", nullable = false)
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
	
	@Column(name="GIVES_CERTIFICATE", nullable = false)
	private boolean givesCertificate;
	
	@Lob
	@Column(name="COVER_PHOTO", length=100000)
	private byte[] coverPhoto;

	@ManyToOne
	@PrimaryKeyJoinColumn(name="CORDER_ID")
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
		return name;
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
	public void setCategoty(Category category) {
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
	
	public Course() {
		
	}
	
	public Course(int id, String name, String description, String teacherName, int duration, Level level,
			Category category, double price, boolean givesCertificate, byte[] coverPhoto) {
		super();
		this.id = id;
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
