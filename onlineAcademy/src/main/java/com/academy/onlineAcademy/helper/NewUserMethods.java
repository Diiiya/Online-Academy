package com.academy.onlineAcademy.helper;

import javax.persistence.PersistenceException;

import com.academy.onlineAcademy.controller.PersonController;
import com.academy.onlineAcademy.exceptions.NewUserException;
import com.academy.onlineAcademy.exceptions.NewUserException.NewUserErrorType;
import com.academy.onlineAcademy.model.Type;
import com.academy.onlineAcademy.model.Person;
import com.academy.onlineAcademy.view.AdminAddUserView;
import com.academy.onlineAcademy.view.SignUpView;
import com.vaadin.data.Binder;
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
	
	public static void addUser(Binder<Person> binder, Type userType, String fullName, String username, String email, String password, String confirmPassword) {
		try {
			setFieldsValues(fullName, username, email, password, confirmPassword);
			checkValidation(userType, binder);
			existingUsername();
			existingEmail();
			matchingPasswords();
			addUserToDatabase();
		}
		catch(NewUserException ex) {
			if (ex.getNewUserErrorType() == NewUserErrorType.FAILED_VALIDATION) {
				Notification notif = new Notification("Warning", "Correct the field(s) in red.",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			}
			else if (ex.getNewUserErrorType() == NewUserErrorType.NO_USER_TYPE) {
				Notification notif = new Notification("Warning", "ERROR!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			}
			else if (ex.getNewUserErrorType() == NewUserErrorType.EXISTING_USERNAME) {
				Notification notif = new Notification("Warning", "The username already exists! Please use another one or log in!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			}
			else if (ex.getNewUserErrorType() == NewUserErrorType.EXISTING_EMAIL) {
				Notification notif = new Notification("Warning", "The email already exists! Please log in!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			}
			else if (ex.getNewUserErrorType() == NewUserErrorType.PASSWORDS_NOT_MATCHING) {
				Notification notif = new Notification("Warning", "The fields for password and confrim password do not match!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			}
			else if(ex.getNewUserErrorType() == NewUserErrorType.DATABASE_FAIL) {
				Notification notif = new Notification("Warning", "Saving to database failed!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			}
		}
		
	}
	
	public static void setFieldsValues(String fullName, String username, String email, String password, String confirmPassword) {
		
			enteredFullName = fullName;
			enteredUsername = username;
			enteredEmail = email;
			enteredPassword = password;
			enteredConfirmPassword = confirmPassword;
		
	}
	
	public static void checkValidation(Type userType, Binder<Person> binder) throws NewUserException{
		if(userType == Type.ADMIN) {
			if (binder.validate().isOk() == true) {
				
			}
			else { 
				throw new NewUserException(NewUserErrorType.FAILED_VALIDATION);
			}
		}
		else if (userType == Type.USER) {
			if (binder.validate().isOk() == true) {
				
			}
			else { 
				throw new NewUserException(NewUserErrorType.FAILED_VALIDATION);
			}
		}
		else {
			throw new NewUserException(NewUserErrorType.NO_USER_TYPE);
		}
	}
	
	public static void existingUsername() throws NewUserException {
		try {
	    	databaseUsername = obj.getPersonByUsername(enteredUsername.toUpperCase()).getUsername();
	    	throw new NewUserException(NewUserErrorType.EXISTING_USERNAME);
	    	
	     }
		catch (PersistenceException e) {
		 }
	}
	
	public static void existingEmail() throws NewUserException {
		try {
	    	databaseEmail = obj.getPersonByEmail(enteredEmail.toUpperCase()).getEmail();
	    	throw new NewUserException(NewUserErrorType.EXISTING_EMAIL);	    	
	     }
		 catch (PersistenceException ex) {
		 }
		
	}
	
	public static void matchingPasswords() throws NewUserException {
		if (enteredPassword.equals(enteredConfirmPassword)) {
			
		}
		else {
			throw new NewUserException(NewUserErrorType.PASSWORDS_NOT_MATCHING);
		}	
	}
	
	public static void addUserToDatabase() throws NewUserException {
		try {
			obj.addPerson(enteredFullName, enteredUsername.toUpperCase(), enteredEmail.toUpperCase(), enteredPassword, null, Type.USER, null, null);
			
			Notification notif = new Notification("Confirmation", "The user has been created!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
		catch(PersistenceException ex) {
			throw new NewUserException(NewUserErrorType.DATABASE_FAIL);
		}
	}
	
}
