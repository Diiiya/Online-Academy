package com.academy.onlineAcademy.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.exceptions.NewCourseException;
import com.academy.onlineAcademy.exceptions.NewCourseException.NewCourseTypeError;
import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.vaadin.data.Binder;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Notification;

public class NewCourseMethods {
	
	static Logger logger = Logger.getLogger(NewCourseMethods.class.getName());
	
	private CourseController courseObj = new CourseController();
	private byte[] convertedCoverPhoto;
	private FileInputStream fileStream = null;
	
	/**
	 * Calls methods to add a new course
	 * @param binder
	 * @param name - course name
	 * @param description - course description
	 * @param teacherName - teacher of the course
	 * @param enteredCoverPhoto - the cover image for the course as a file
	 * @param duration - course duration
	 * @param level - level of difficulty of the course
	 * @param category - category to which the course belongs to
	 * @param price - price of the course
	 * @param givesCertificate - if the course awards a certificate or not
	 */
	public void addNewCourse(Binder<Course> binder, String name, String description, String teacherName, File enteredCoverPhoto, int duration, Level level,
			Category category, double price, boolean givesCertificate) {
		try {
			convertInputPhoto(enteredCoverPhoto);
			existingCourseCheck(name);
			addCourseToDatabase(name, description, teacherName, duration, level, category, price, givesCertificate);
		}
		catch (NewCourseException ex){
			if (ex.getNewCourseTypeError() == NewCourseTypeError.VALIDATION_FAILED) {
				Notification notif = new Notification("Warning", "Correct the field(s) in red.",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(java.util.logging.Level.SEVERE, "Not all fields are filled in as expected!", ex);
			}
			else if (ex.getNewCourseTypeError() == NewCourseTypeError.EXISTING_COURSE) {
				Notification notif = new Notification("Warning", "A course with the same name already exists! Please review or use a different name!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(java.util.logging.Level.SEVERE, "A couse " + name + " already exists! Another name should be given!", ex);
			}
		}
		
	}
	
	/**
	 * Converts an image file to a file stream
	 * @param photoFileInput - the image file for the course cover
	 */
	private void convertInputPhoto(File photoFileInput) {
		try {
		    fileStream = new FileInputStream(photoFileInput);
			convertedCoverPhoto = fileStream.readAllBytes();
		}
		catch (Exception ex) {
			
		}
		finally {
			if (fileStream != null) {
				try {
					fileStream.close();
				} 
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Checks if another course with the same name exists in the database
	 * @param name - unique name of the course
	 * @throws NewCourseException - if another course with the same name exists, throws an exception
	 */
	private void existingCourseCheck(String name) throws NewCourseException {
		try {
	    	courseObj.getCourseByName(name.toUpperCase()).getName();
	    	throw new NewCourseException(NewCourseTypeError.EXISTING_COURSE);
	     }
		 catch (PersistenceException e) {
		 }
	}
	
	/**
	 * Add a new course to the database
	 * @param name - course name
	 * @param description - course description
	 * @param teacherName - teacher of the course
	 * @param duration - course duration
	 * @param level - level of difficulty of the course
	 * @param category - category to which the course belongs to
	 * @param price - price of the course
	 * @param givesCertificate - if the course awards a certificate or not
	 */
	private void addCourseToDatabase(String name, String description, String teacherName, int duration, Level level,
			Category category, double price, boolean givesCertificate) {
		try {
			courseObj.addCourse(name.toUpperCase(), description, teacherName, duration, level, category, price, givesCertificate, convertedCoverPhoto);
			
			Notification notif = new Notification("Confirmation", "The course has been created!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			
			logger.log(java.util.logging.Level.INFO, "The course has been created!");
		}
		catch(Exception ex2) {
			ex2.printStackTrace();
		}
	}

}
