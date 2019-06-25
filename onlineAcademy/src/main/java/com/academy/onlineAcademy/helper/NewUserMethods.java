package com.academy.onlineAcademy.helper;

import com.academy.onlineAcademy.controller.PersonController;
import com.academy.onlineAcademy.model.Type;
import com.academy.onlineAcademy.view.AdminAddUserView;
import com.academy.onlineAcademy.view.SignUpView;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

public class NewUserMethods {
	
	static PersonController obj = new PersonController();
	static String enteredFullName;
	static String enteredUsername;
	static String databaseUsername;
	static String enteredEmail;
	static String databaseEmail;
	static String enteredPassword;
	static String enteredConfirmPassword;

	public static void checkEmptyFields(int userTypeId, String fullName, String username, String email, String password, String confirmPassword) {
		if (fullName != "" && username != "" && email != "" && password != "" && confirmPassword != "") {
			enteredFullName = fullName;
			enteredUsername = username;
			enteredEmail = email;
			enteredPassword = password;
			enteredConfirmPassword = confirmPassword;
			checkValidation(userTypeId);
		}
		else {
			Notification notif = new Notification("Warning", "All required fields (*) should be filled in!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		} 
	}
	
	public static void checkValidation(int userTypeId) {
		if(userTypeId == 1) {
			if (AdminAddUserView.binder.validate().isOk() == true) {
	            existingUsername();
			}
			else { 
				Notification notif = new Notification("Warning", "Correct the field(s) in red.",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			}
		}
		else if (userTypeId == 2) {
			if (SignUpView.binder.validate().isOk() == true) {
	            existingUsername();
			}
			else { 
				Notification notif = new Notification("Warning", "Correct the field(s) in red.",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			}
		}
		else {
			Notification notif = new Notification("Warning", "ERROR!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
	}
	
	public static void existingUsername() {
		try {
	    	databaseUsername = obj.getPersonByUsername(enteredUsername.toUpperCase()).getUsername();
	    	Notification notif = new Notification("Warning", "The username already exists! Please use another one or log in!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
	    	
	     }
		 catch (Exception ex) {
			 existingEmail();
		 }
	}
	
	public static void existingEmail() {
		try {
	    	databaseEmail = obj.getPersonByEmail(enteredEmail.toUpperCase()).getEmail();
	    	Notification notif = new Notification("Warning", "The email already exists! Please log in!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
	    	
	     }
		 catch (Exception ex) {
			 matchingPasswords();
		 }
	}
	
	public static void matchingPasswords() {
		if (enteredPassword.equals(enteredConfirmPassword)) {
			addUserToDatabase();
		}
		else {
			Notification notif = new Notification("Warning", "The fields for password and confrim password do not match!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}	
	}
	
	public static void addUserToDatabase() {
		try {
			obj.addPerson(enteredFullName, enteredUsername.toUpperCase(), enteredEmail.toUpperCase(), enteredPassword, null, Type.USER, null, null);
			
			Notification notif = new Notification("Confirmation", "The user has been created!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
		catch(Exception ex2) {
			ex2.printStackTrace();
		}
	}
	
}
