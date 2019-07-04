package com.academy.onlineAcademy.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
	
	private static CourseController courseObj = new CourseController();
	private static String enteredName;
//	private static File enteredCoverPhoto;
	private static byte[] convertedCoverPhoto;
	private static String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	private static FileInputStream fileStream = null;
	
	public static void addNewCourse(Binder<Course> binder, String name, String description, String teacherName, int duration, Level level,
			Category category, double price, boolean givesCertificate /* , File coverPhoto */) {
		try {
			//convertInputPhoto(enteredCoverPhoto);
			existingCourseCheck(name);
			addCourseToDatabase(name, description, teacherName, duration, level, category, price, givesCertificate);
		}
		catch (NewCourseException ex){
			if (ex.getNewCourseTypeError() == NewCourseTypeError.VALIDATION_FAILED) {
				Notification notif = new Notification("Warning", "Correct the field(s) in red.",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			}
			else if (ex.getNewCourseTypeError() == NewCourseTypeError.EXISTING_COURSE) {
				Notification notif = new Notification("Warning", "A course with the same name already exists! Please review or use a different name!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			}
		}
		
	}
	
	private static void convertInputPhoto(File photoFileInput) {
		try {
		fileStream = new FileInputStream(new File(basepath + "/1online-courses_0.jpg"));
	    //fileStream = new FileInputStream(photoFileInput);
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
	
	private static void existingCourseCheck(String name) throws NewCourseException {
		try {
	    	courseObj.getCourseByName(name.toUpperCase()).getName();
	    	throw new NewCourseException(NewCourseTypeError.EXISTING_COURSE);
	     }
		 catch (PersistenceException e) {
		 }
	}
	
	private static void addCourseToDatabase(String name, String description, String teacherName, int duration, Level level,
			Category category, double price, boolean givesCertificate) {
		try {
			courseObj.addCourse(name.toUpperCase(), description, teacherName, duration, level, category, price, givesCertificate, convertedCoverPhoto);
			
			Notification notif = new Notification("Confirmation", "The course has been created!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
		catch(Exception ex2) {
			ex2.printStackTrace();
		}
	}

}
