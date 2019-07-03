package com.academy.onlineAcademy.helper;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.exceptions.CourseException;
import com.academy.onlineAcademy.model.Course;
import com.vaadin.data.Binder;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

public class UpdateCourseMethods {

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
			}
			else if (ex.getCourseErrorType() == CourseException.CourseErrorType.FIELDS_VALIDATION) {
				Notification notif = new Notification("Warning", "Please correct the fields in red!", Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			}
			else if (ex.getCourseErrorType() == CourseException.CourseErrorType.DATABASE_UPDATE_FAILED) {
				Notification notif = new Notification("Warning", "Unexpected error!", Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			}
		}
		return false;
		
	}
	
	public static void existingCourse(int courseId, CourseController courseObj, Course selectedCourse, String courseName) throws CourseException {
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
	
	public static void writeBean(Binder<Course> binder, Course selectedCourse) throws CourseException {
		try {
			binder.writeBean(selectedCourse);
		}
		catch(Exception ex) {
			throw new CourseException(CourseException.CourseErrorType.FIELDS_VALIDATION);
		}
	}
	
	public static void updateInDatabase(CourseController courseObj, Course selectedCourse) throws CourseException {
		try {
			courseObj.updateCourseById(selectedCourse);
			Notification notif = new Notification("Confirmation!", "Course successfully updated!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
		catch (Exception ex) {
			throw new CourseException(CourseException.CourseErrorType.DATABASE_UPDATE_FAILED);
		}
	}
	
}
