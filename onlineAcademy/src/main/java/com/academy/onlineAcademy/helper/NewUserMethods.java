package com.academy.onlineAcademy.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	private static Logger logger = Logger.getLogger(NewUserMethods.class.getName());
	
	private PersonController obj = new PersonController();
	
	/**
	 * Adds a new user to the database calling 5 submethods
	 * @param binder
	 * @param userType - the type of user that creates the new user
	 * @param fullName
	 * @param username
	 * @param email
	 * @param password
	 * @param confirmPassword
	 * @param userTypeCreated - the type of user that has to be created
	 */
	public void addUser(Binder<Person> binder, Type userType, String fullName, String username, String email, String password, String confirmPassword, String userTypeCreated) {
		try {
			checkValidation(userType, binder);
			existingUsername(username);
			existingEmail(email);
			matchingPasswords(password, confirmPassword);
			addUserToDatabase(userType, fullName, username, email, password, userTypeCreated);
		}
		catch(NewUserException ex) {
			if (ex.getNewUserErrorType() == NewUserErrorType.FAILED_VALIDATION) {
				Notification notif = new Notification("Warning", "Correct the field(s) in red.",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "Not all fields are filled in as expected!", ex);
			}
			else if (ex.getNewUserErrorType() == NewUserErrorType.NO_USER_TYPE) {
				Notification notif = new Notification("Warning", "ERROR!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "The type " + userType + " is not recognized.", ex);
			}
			else if (ex.getNewUserErrorType() == NewUserErrorType.EXISTING_USERNAME) {
				Notification notif = new Notification("Warning", "The username already exists! Please use another one or log in!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "The username " + username + " already exists!", ex);
			}
			else if (ex.getNewUserErrorType() == NewUserErrorType.EXISTING_EMAIL) {
				Notification notif = new Notification("Warning", "The email already exists! Please log in!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "The email " + email + " already exists!", ex);
			}
			else if (ex.getNewUserErrorType() == NewUserErrorType.PASSWORDS_NOT_MATCHING) {
				Notification notif = new Notification("Warning", "The fields for password and confrim password do not match!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "The password and confrim password values do not match!", ex);
			}
			else if(ex.getNewUserErrorType() == NewUserErrorType.DATABASE_FAIL) {
				Notification notif = new Notification("Warning", "Saving to database failed!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "Failed to save the new user to the database!", ex);
			}

		}
		
	}
	
	/**
	 * Checks if the binder is valid
	 * @param userType
	 * @param binder
	 * @throws NewUserException thrown if the binder is invalid
	 */
	private void checkValidation(Type userType, Binder<Person> binder) throws NewUserException{
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
	
	/**
	 * Checks if another user with the same username already exists in the database
	 * @param username
	 * @throws NewUserException thrown if the username already exists
	 */
	private void existingUsername(String username) throws NewUserException {
		try {
	    	obj.getPersonByUsername(username.toUpperCase());
	    	throw new NewUserException(NewUserErrorType.EXISTING_USERNAME);
	    	
	     }
		catch (PersistenceException e) {
		 }
	}
	
	/**
	 * Checks if another user with the same email already exists in the database
	 * @param email
	 * @throws NewUserException thrown if the email already exists
	 */
	private void existingEmail(String email) throws NewUserException {
		try {
	    	obj.getPersonByEmail(email.toUpperCase());
	    	throw new NewUserException(NewUserErrorType.EXISTING_EMAIL);	    	
	     }
		 catch (PersistenceException ex) {
		 }
		
	}
	
	/**
	 * Checks if the password and confirm password fields have the same values
	 * @param password
	 * @param confirmPassword
	 * @throws NewUserException thrown if the two values differ
	 */
	private void matchingPasswords(String password, String confirmPassword) throws NewUserException {
		if (password.equals(confirmPassword)) {
			
		}
		else {
			throw new NewUserException(NewUserErrorType.PASSWORDS_NOT_MATCHING);
		}	
	}
	
	/**
	 * Adds the new user to the database
	 * @param userTypeCreator
	 * @param fullName
	 * @param username
	 * @param email
	 * @param password
	 * @param userTypeCreated
	 * @throws NewUserException thrown if the user fails to be added to the database
	 */
	private void addUserToDatabase(Type userTypeCreator, String fullName, String username, String email, String password, String userTypeCreated) throws NewUserException {
		try {
			if(userTypeCreator == Type.ADMIN) {
				 obj.addPerson(fullName, username.toUpperCase(), email, password, null, Type.valueOf(userTypeCreated), null, null);
			}
			else {
			    obj.addPerson(fullName, username.toUpperCase(), email, password, null, Type.USER, null, null);
			}
			
			Notification notif = new Notification("Confirmation", "The user has been created!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			
			logger.log(Level.INFO, "User " + username + " has been created.");
		}
		catch(PersistenceException ex) {
			throw new NewUserException(NewUserErrorType.DATABASE_FAIL);
		}
	}
	
}
