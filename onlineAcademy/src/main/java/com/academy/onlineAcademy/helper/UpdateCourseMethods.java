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

	/**
	 * Get the data for a specific course that will be updated
	 * @param selectedCourseId
	 * @param binder
	 * @param courseObj
	 * @param selectedCourse
	 */
	public void getCourseInfo(int selectedCourseId, Binder<Course> binder, CourseController courseObj, Course selectedCourse) {
		try {
			selectedCourse = courseObj.getCourseById(selectedCourseId);
			binder.readBean(selectedCourse);
			
		}
		catch(Exception ex) {
		}
	}
	
	/**
	 * Update a course by calling three submethods
	 * @param courseId
	 * @param binder
	 * @param courseObj
	 * @param selectedCourse
	 * @param courseName
	 * @return boolean if the course has been updated
	 */
	public boolean updateCourse(int courseId, Binder<Course> binder, CourseController courseObj, Course selectedCourse, String courseName) {
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
				
				logger.log(Level.SEVERE, "A course with name " + courseName + " already exists. Use another name!", ex);
			}
			else if (ex.getCourseErrorType() == CourseException.CourseErrorType.FIELDS_VALIDATION) {
				Notification notif = new Notification("Warning", "Please correct the fields in red!", Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "Not all fields are filled in as required. Validation failed!", ex);
			}
			else if (ex.getCourseErrorType() == CourseException.CourseErrorType.DATABASE_UPDATE_FAILED) {
				Notification notif = new Notification("Warning", "Unexpected error!", Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "Failed to update the course in the database.", ex);
			}
		}
		return false;
		
	}
	
	/**
	 * Checks if a course with the same name already exists in the database
	 * @param courseId
	 * @param courseObj
	 * @param selectedCourse
	 * @param courseName
	 * @throws CourseException thrown if another course with the same name already exists
	 */
	private void existingCourse(int courseId, CourseController courseObj, Course selectedCourse, String courseName) throws CourseException {
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
	
	/**
	 * Validates/Writes the new course object/bean
	 * @param binder
	 * @param selectedCourse
	 * @throws CourseException thrown if the course fails cannot be validated
	 */
	private void writeBean(Binder<Course> binder, Course selectedCourse) throws CourseException {
		try {
			binder.writeBean(selectedCourse);
		}
		catch(Exception ex) {
			throw new CourseException(CourseException.CourseErrorType.FIELDS_VALIDATION);
		}
	}
	
	/**
	 * Updates the selected course in the database
	 * @param courseObj
	 * @param selectedCourse
	 * @throws CourseException - thrown if the course fails to be updated in the database
	 */
	private void updateInDatabase(CourseController courseObj, Course selectedCourse) throws CourseException {
		try {
			String courseName = selectedCourse.getName().toUpperCase();
			selectedCourse.setName(courseName);
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
