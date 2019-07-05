package com.academy.onlineAcademy.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.exceptions.CourseException;
import com.academy.onlineAcademy.model.Course;
import com.vaadin.data.Binder;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

public class UpdateCourseMethods {
	
	private static Logger logger = Logger.getLogger(UpdateCourseMethods.class.getName());

	public static void getCourseInfo(int selectedCourseId, Binder<Course> binder, CourseController courseObj, Course selectedCourse) {
		try {
			selectedCourse = courseObj.getCourseById(selectedCourseId);
			binder.readBean(selectedCourse);
			
		}
		catch(Exception ex) {
		}
	}
	
	public static boolean updateCourse(int courseId, Binder<Course> binder, CourseController courseObj, Course selectedCourse, String courseName) {
		try {
			existingCourse(courseId, courseObj, selectedCourse, courseName);
			writeBean(binder, selectedCourse);
			updateInDatabase(courseObj, selectedCourse);
			return true;
		}
		catch (CourseException ex) {
			if (ex.getCourseErrorType() == CourseException.CourseErrorType.EXISTING_COURSE) {
				Notification notif = new Notification("Warning", "Course with the same name already exists. Please choose another name.", Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "A course with name " + courseName + " already exists. Use another name!");
			}
			else if (ex.getCourseErrorType() == CourseException.CourseErrorType.FIELDS_VALIDATION) {
				Notification notif = new Notification("Warning", "Please correct the fields in red!", Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "Not all fields are filled in as required. Validation failed!");
			}
			else if (ex.getCourseErrorType() == CourseException.CourseErrorType.DATABASE_UPDATE_FAILED) {
				Notification notif = new Notification("Warning", "Unexpected error!", Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "Failed to update the course in the database.");
			}
		}
		return false;
		
	}
	
	private static void existingCourse(int courseId, CourseController courseObj, Course selectedCourse, String courseName) throws CourseException {
		try {
			selectedCourse = courseObj.getCourseByName(courseName);
		    	if (courseName.equals(selectedCourse.getName())) {
		    		
		    	}
		    	else {
		    		throw new CourseException(CourseException.CourseErrorType.EXISTING_COURSE);
		    	}
		}
		catch(Exception ex) {
			
		}
		
	}
	
	private static void writeBean(Binder<Course> binder, Course selectedCourse) throws CourseException {
		try {
			binder.writeBean(selectedCourse);
		}
		catch(Exception ex) {
			throw new CourseException(CourseException.CourseErrorType.FIELDS_VALIDATION);
		}
	}
	
	private static void updateInDatabase(CourseController courseObj, Course selectedCourse) throws CourseException {
		try {
			courseObj.updateCourseById(selectedCourse);
			Notification notif = new Notification("Confirmation!", "Course successfully updated!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			
			logger.log(Level.INFO, "The course has been successfully updated!");
		}
		catch (Exception ex) {
			throw new CourseException(CourseException.CourseErrorType.DATABASE_UPDATE_FAILED);
		}
	}
	
}
