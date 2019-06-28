package com.academy.onlineAcademy.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.vaadin.data.Binder;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Notification;

public class NewCourseMethods {
	
	private static CourseController obj = new CourseController();
	private static String enteredName;
	private static String enteredDescription;
	private static String enteredTeacherName;
	private static int enteredDuration;
	private static Level enteredLevel;
	private static Category enteredCategory;
	private static double enteredPrice;
	private static boolean enteredCertificateVal;
//	private static File enteredCoverPhoto;
	private static byte[] convertedCoverPhoto;
	private static String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	private static FileInputStream fileStream = null;
	
//	public static void addNewCourse(Binder<Course> binder, String name, String description, String teacherName, int duration, Level level,
//			Category category, double price, boolean givesCertificate /* , File coverPhoto */) {
//		checkEmptyFields(name, description, teacherName, duration, level, category, price, givesCertificate /*, coverPhoto */);
//		boolean fieldsAreValid = checkValidation(binder);
//		if (fieldsAreValid == true) {
//			//convertInputPhoto();
//			boolean courseExists = existingCourseCheck();
//			if (courseExists == true) {
//				addCourseToDatabase();
//			}
//		}
//		
//	}
	
	public static void addNewCourse(Binder<Course> binder, String name, String description, String teacherName, int duration, Level level,
			Category category, double price, boolean givesCertificate /* , File coverPhoto */) {
		checkEmptyFields(binder, name, description, teacherName, duration, level, category, price, givesCertificate /*, coverPhoto */);
	}
	
	public static void checkEmptyFields(Binder<Course> binder, String name, String description, String teacherName, int duration, Level level,
			Category category, double price, boolean givesCertificate /* , File coverPhoto */) {
		if (name != "" && description != "" && teacherName != "" && duration != 0 && level != null && category != null &&
				price != 0 /* && coverPhoto != null */) {
			
			enteredName = name;
			enteredDescription = description;
			enteredTeacherName = teacherName;
			enteredDuration = duration;
			enteredLevel = level;
			enteredCategory = category;
			enteredPrice = price;
			enteredCertificateVal = givesCertificate;
			//enteredCoverPhoto = coverPhoto;
			
			checkValidation(binder);
		}
		else {
			Notification notif = new Notification("Warning", "All required fields (*) should be filled in! Numeric values (for duration and price) cannot be 0!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		} 
	}
	
	public static void checkValidation(Binder<Course> binder) {
			if (binder.validate().isOk() == true) {
				//convertInputPhoto(enteredCoverPhoto);
				existingCourseCheck();
			}
			else { 
				Notification notif = new Notification("Warning", "Correct the field(s) in red.",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			}
	}
	
	public static void convertInputPhoto(File photoFileInput) {
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
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static void existingCourseCheck() {
		try {
	    	obj.getCourseByName(enteredName.toUpperCase()).getName();
	    	Notification notif = new Notification("Warning", "A course with the same name already exists! Please review or use a different name!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
	     }
		 catch (Exception ex) {
			 addCourseToDatabase();
		 }
	}
	
	public static void addCourseToDatabase() {
		try {
			obj.addCourse(enteredName.toUpperCase(), enteredDescription, enteredTeacherName, enteredDuration, enteredLevel, enteredCategory, 
					enteredPrice, enteredCertificateVal, convertedCoverPhoto);
			
			Notification notif = new Notification("Confirmation", "The course has been created!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
		catch(Exception ex2) {
			ex2.printStackTrace();
		}
	}

}
