package com.academy.onlineAcademy.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.controller.OrderController;
import com.academy.onlineAcademy.controller.PersonController;
import com.academy.onlineAcademy.exceptions.OrderException;
import com.academy.onlineAcademy.exceptions.OrderException.OrderErrorType;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Order;
import com.academy.onlineAcademy.model.Person;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

public class NewOrderMethods {
	
	private static Logger logger = Logger.getLogger(NewOrderMethods.class.getName());
	
	private Person person;
	private OrderController orderObj = new OrderController();
	private PersonController personObj = new PersonController();
	private CourseController courseObj = new CourseController();
	
	///////////////
	// FOR ADMIN
	//////////////
	/**
	 * Methods that gets all the course names from the database. When admin creates an order he sees that list with the available courses
	 * @param courseObj
	 * @return List<String> - list with the names of the courses in the database
	 */
	public List<String> getCourses(CourseController courseObj) {
		
		List<String> courseNames = new ArrayList<>();
		List<Course> courses;
		courses = courseObj.getAllCourses();
		for (Course course : courses) {
			courseNames.add(course.getName());
			}
		
		return courseNames;
	}
	
	/**
	 * Calls two other methods before placing an order as ADMIN
	 * @param userEmail
	 * @param courseName
	 */
	public void placeOrder(String userEmail, String courseName) {
		try {
			int courseId = getCourseId(courseName);
			checkIfEmailExists(userEmail);
			createNewOrder(courseId);
		}
		catch(OrderException e) {
			if (e.getOrderErrorType() == OrderErrorType.NOT_EXISTING_EMAIL) {
				Notification notif = new Notification("Warning", "There is no user with this email address!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "No user with email " + userEmail + " exists. Use an existing email!", e);
			}
			else if (e.getOrderErrorType() == OrderErrorType.ORDER_FAILED) {
				Notification notif = new Notification("Warning", "Unexpected error!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(java.util.logging.Level.SEVERE, "Failed to save the new order to the database.", e);
			}

		}
	}
	
	/**
	 * Checks if the admin has entered an existing email or not
	 * @param email - the email of the user for which the order will be placed
	 * @throws OrderException - thrown if the email doesn't exists
	 */
	private void checkIfEmailExists(String email) throws OrderException {
		try {
			person = personObj.getPersonByEmail(email.toUpperCase());
		}
		catch (Exception ex) {
			throw new OrderException(com.academy.onlineAcademy.exceptions.OrderException.OrderErrorType.NOT_EXISTING_EMAIL);
		}
	}
	
	/**
	 * Adds a new order to the database
	 * @param courseId
	 * @throws OrderException - thrown if the order fails to be saved to the database
	 */
	private void createNewOrder(int courseId) throws OrderException {
		try {
			Date date = new Date();
			Order newOrder = new Order(person.getId(), courseId, date, true, 0);
			orderObj.addOrder(newOrder);
			Notification notif = new Notification("Confirmation", "The order has been placed! Order id: " + newOrder.getId(),
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			
			logger.log(java.util.logging.Level.INFO, "The order has been placed");
		}
		catch (Exception e) {
			throw new OrderException(com.academy.onlineAcademy.exceptions.OrderException.OrderErrorType.ORDER_FAILED);
		}
		
	}
	
	/**
	 * Methods that gets the id of a course based on its name
	 * @param courseName
	 * @return int - the id of the course
	 */
	private int getCourseId(String courseName) {
		Course course = courseObj.getCourseByName(courseName.toUpperCase());
		int courseId = course.getId();
		return courseId;
	}
	
	//////////////
	// FOR USER
	/////////////
	/**
	 * Adds an order to the database as USER
	 * @param userId
	 * @param selectedCourse
	 */
	public void placeOrder(int userId, Course selectedCourse) {
		Date date = new Date();
		if(userId == 0) {
			Notification notif = new Notification("Warning","Please log in to continue ... ",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			
			logger.log(Level.SEVERE, "No user has been logged in, to place order!");
		}
		else {
			Order newOrder = new Order(userId, selectedCourse.getId(), date, false, selectedCourse.getPrice());
			orderObj.addOrder(newOrder);
			
			Notification notif = new Notification("Confirmation","The course has been added!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			
			logger.log(Level.INFO, "The course has been added!");
		}
	}
}
