package com.academy.onlineAcademy.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.controller.OrderController;
import com.academy.onlineAcademy.controller.PersonController;
import com.academy.onlineAcademy.exceptions.OrderException;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Order;
import com.academy.onlineAcademy.model.Person;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

public class NewOrderMethods {
	
	private static Person person;
	
	public static List<String> getCourses(CourseController courseObj) {
		
		List<String> courseNames = new ArrayList<>();
		List<Course> courses;
		courses = courseObj.getAllCourses();
		for (Course course : courses) {
			courseNames.add(course.getName());
			}
		
		return courseNames;
	}
	
	public static void placeOrder(String userEmail, PersonController personObj, OrderController orderObj, String courseName, CourseController courseObj) {
		try {
			int courseId = getCourseId(courseName, courseObj);
			checkIfEmailExists(userEmail, personObj);
			createNewOrder(courseId, orderObj);
		}
		catch(OrderException e) {
			if (e.getOrderErrorType() == com.academy.onlineAcademy.exceptions.OrderException.OrderErrorType.NOT_EXISTING_EMAIL) {
				Notification notif = new Notification("Warning", "The is no user with this email address!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			}
			else if (e.getOrderErrorType() == com.academy.onlineAcademy.exceptions.OrderException.OrderErrorType.ORDER_FAILED) {
				Notification notif = new Notification("Warning", "Unexpected error!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			}

		}
	}
	
	private static void checkIfEmailExists(String email, PersonController personObj) throws OrderException {
		try {
			person = personObj.getPersonByEmail(email.toUpperCase());
		}
		catch (Exception ex) {
			throw new OrderException(com.academy.onlineAcademy.exceptions.OrderException.OrderErrorType.NOT_EXISTING_EMAIL);
		}
	}
	
	private static void createNewOrder(int courseId, OrderController orderObj) throws OrderException {
		try {
			Date date = new Date();
			Order newOrder = new Order(person.getId(), courseId, date, true, 0);
			orderObj.addOrder(newOrder);
			Notification notif = new Notification("Confirmation", "The order has been placed! Order id: " + newOrder.getId(),
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
		catch (Exception e) {
			throw new OrderException(com.academy.onlineAcademy.exceptions.OrderException.OrderErrorType.ORDER_FAILED);
		}
		
	}
	
	private static int getCourseId(String courseName, CourseController courseObj) {
		Course course = courseObj.getCourseByName(courseName);
		int courseId = course.getId();
		return courseId;
	}

}
