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
	
	static CourseController obj = new CourseController();
	static String enteredName;
	static String databaseName;
	static String enteredDescription;
	static String enteredTeacherName;
	static int enteredDuration;
	static Level enteredLevel;
	static Category enteredCategory;
	static double enteredPrice;
	static boolean enteredCertificateVal;
	static File enteredCoverPhoto;
	static byte[] convertedCoverPhoto;
	static String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	static FileInputStream fileStream = null;
	
	public static void addNewCourse(Binder<Course> binder, String name, String description, String teacherName, int duration, Level level,
			Category category, double price, boolean givesCertificate /* , File coverPhoto */) {
		checkEmptyFields(name, description, teacherName, duration, level, category, price, givesCertificate /*, coverPhoto */);
		checkValidation(binder);
		//convertInputPhoto();
		existingCourseCheck();
		addCourseToDatabase();
	}
	
	public static void checkEmptyFields(String name, String description, String teacherName, int duration, Level level,
			Category category, double price, boolean givesCertificate /* , File coverPhoto */) {
		if (name != "" && description != "" && teacherName != "" && duration != 0 && level != null && category != null &&
				price != 0 /* && coverPhoto != null */) {
			
			System.out.print("Empty fields checked!");
			
			enteredName = name;
			enteredDescription = description;
			enteredTeacherName = teacherName;
			enteredDuration = duration;
			enteredLevel = level;
			enteredCategory = category;
			enteredPrice = price;
			enteredCertificateVal = givesCertificate;
			//enteredCoverPhoto = coverPhoto;
			
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
				addCourseToDatabase();
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
	    	databaseName = obj.getCourseByName(enteredName.toUpperCase()).getName();
	    	Notification notif = new Notification("Warning", "A course with the same name already exists! Please review or use a different name!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
	    	
	     }
		 catch (Exception ex) {
			
		 }
	}
	
	public static void addCourseToDatabase() {
		try {
			obj.addCourse(enteredName, enteredDescription, enteredTeacherName, enteredDuration, enteredLevel, enteredCategory, 
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
