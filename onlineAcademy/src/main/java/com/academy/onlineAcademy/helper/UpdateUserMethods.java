package com.academy.onlineAcademy.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.NoResultException;

import com.academy.onlineAcademy.controller.PersonController;
import com.academy.onlineAcademy.exceptions.NewUserException;
import com.academy.onlineAcademy.exceptions.UpdateUserException;
import com.academy.onlineAcademy.exceptions.NewUserException.NewUserErrorType;
import com.academy.onlineAcademy.exceptions.UpdateUserException.UpdateUserExErrorType;
import com.academy.onlineAcademy.model.Person;
import com.academy.onlineAcademy.model.Type;
import com.vaadin.data.Binder;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

public class UpdateUserMethods {
	
	private static Logger logger = Logger.getLogger(UpdateUserMethods.class.getName());
	
	private Person selectedPerson;
	private PersonController personObj = new PersonController();
	private byte[] convertedProfilePhoto;
	
	/**
	 * Gets the selected user details from the database
	 * @param userId
	 * @param binder
	 */
	public void getUserInfo(int userId, Binder<Person> binder) {
		try {
			selectedPerson = personObj.getPersonById(userId);			
			binder.readBean(selectedPerson);
		}
		catch(Exception ex) {
			Notification notif = new Notification("Warning", "Could not load data from the database!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			
			logger.log(Level.SEVERE, "Could not load data from the database!", ex);
		}
	}
	
	public Person getPerson() {
		return selectedPerson;
	}
	
	/**
	 * Updates the user details in the database by calling 4 (for admin) - 5 (for user) other methods
	 * @param userType
	 * @param selectedPerson
	 * @param binder
	 * @param enteredEmail
	 * @param password
	 * @param confirmPassword
	 * @param profileImageFile
	 * @return boolean - if the update has been successful or not
	 */
	public boolean updatePersonSettings(Type userType, Person selectedPerson, Binder<Person> binder, String enteredEmail, String password, String confirmPassword, File profileImageFile) {
		try {
			existingEmail(enteredEmail);
			matchingPasswords(password, confirmPassword);
			writeBean(binder);
			if (userType.equals(Type.USER) ) {
				convertInputPhoto(profileImageFile);
			}
			updateInDatabase();
			return true;
		}
		catch (UpdateUserException ex) {
			if (ex.getUpdateUserExErrorType() == UpdateUserExErrorType.EXISTING_EMAIL) {
				Notification notif = new Notification("Warning", "The email is already used by another user!", Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "Another user is already using " + enteredEmail + " email!", ex);
			}
			else if (ex.getUpdateUserExErrorType() == UpdateUserExErrorType.PASSWORDS_NOT_MATCHING) {
				Notification notif = new Notification("Warning", "The fields for password and confrim password do not match!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "The password and confrim password values do not match!", ex);
			}
			else if (ex.getUpdateUserExErrorType() == UpdateUserExErrorType.VALIDATION_FAILED) {
				Notification notif = new Notification("Warning", "Please correct the fields in red!", Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "Not all fields are filled in as expected! Validation failed!", ex);
			}
			else if (ex.getUpdateUserExErrorType() == UpdateUserExErrorType.DATABASE_FAILED) {
				Notification notif = new Notification("Warning", "Failed to save to database!", Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "Failed to update user in the database!", ex);
			}
		}
		return false;
	}
	
//	public boolean updatePersonSettings(Person selectedPerson, Binder<Person> binder, String enteredEmail, File file) {
//		try {
//			existingEmail(enteredEmail);
//			writeBean(binder);
//			updateInDatabase();
//			return true;
//		}
//		catch (UpdateUserException ex) {
//			if (ex.getUpdateUserExErrorType() == UpdateUserExErrorType.EXISTING_EMAIL) {
//				Notification notif = new Notification("Warning", "The email is already used by another user!", Notification.TYPE_WARNING_MESSAGE);
//				notif.show(Page.getCurrent());
//				
//				logger.log(Level.SEVERE, "Another user is already using " + enteredEmail + " email!", ex);
//			}
//			else if (ex.getUpdateUserExErrorType() == UpdateUserExErrorType.VALIDATION_FAILED) {
//				Notification notif = new Notification("Warning", "Please correct the fields in red!", Notification.TYPE_WARNING_MESSAGE);
//				notif.show(Page.getCurrent());
//				
//				logger.log(Level.SEVERE, "Not all fields are filled in as expected! Validation failed!", ex);
//			}
//			else if (ex.getUpdateUserExErrorType() == UpdateUserExErrorType.DATABASE_FAILED) {
//				Notification notif = new Notification("Warning", "Failed to save to database!", Notification.TYPE_WARNING_MESSAGE);
//				notif.show(Page.getCurrent());
//				
//				logger.log(Level.SEVERE, "Failed to update user in the database!", ex);
//			}
//		}
//		return false;
//	}
	
	/**
	 * Checks if another user with the same email already exists
	 * @param enteredEmail
	 */
	private void existingEmail(String enteredEmail) {
		try {
			personObj.getPersonByEmail(enteredEmail);
			existingEmailUser(selectedPerson, enteredEmail);
		}
		catch (NoResultException exc){
			
		}
		catch (UpdateUserException ex) {
			if (ex.getUpdateUserExErrorType() == UpdateUserExErrorType.EXISTING_EMAIL) {
				Notification notif = new Notification("Warning", "The email is already used by another user!", Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "Another user is already using " + enteredEmail + " email!", ex);
			}
		}	
	}
	
	private void existingEmailUser(Person selectedPerson, String enteredEmail) throws UpdateUserException{
		if (selectedPerson.getEmail().equals(enteredEmail)) {
    	}
    	else {
    		throw new UpdateUserException(UpdateUserExErrorType.EXISTING_EMAIL);
    	}
	}
	
	/**
	 * Checks if the password and confirm password field values match
	 * @param password
	 * @param confirmPassword
	 * @throws UpdateUserException thrown if the two values don't match
	 */
	private void matchingPasswords(String password, String confirmPassword) throws UpdateUserException {
		if (password != null) {
			if (password.equals(confirmPassword)) {
			}
			else {
				throw new UpdateUserException(UpdateUserExErrorType.PASSWORDS_NOT_MATCHING);
			}
		}	
	}
	
	/**
	 * Validates the new user details
	 * @param binder
	 * @throws UpdateUserException thrown if the validation fails
	 */
	private void writeBean(Binder<Person> binder) throws UpdateUserException{
		try {
			binder.writeBean(selectedPerson);
		}
		catch(Exception ex) {
			throw new UpdateUserException(UpdateUserExErrorType.VALIDATION_FAILED);
		}
	}
	
	/**
	 * Converts the input image file to file stream
	 * @param photoFileInput
	 */
	private void convertInputPhoto(File photoFileInput) {
		FileInputStream fileStream = null;
		try {
		    fileStream = new FileInputStream(photoFileInput);
			convertedProfilePhoto = fileStream.readAllBytes();
			selectedPerson.setPhoto(convertedProfilePhoto);
		}
		catch (Exception ex) {
			
		}
		finally {
			if (fileStream != null) {
				try {
					fileStream.close();
				} 
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Updates the user object in the database
	 * @throws UpdateUserException
	 */
	private void updateInDatabase() throws UpdateUserException {
		try {
			personObj.updatePerson(selectedPerson);
			Notification notif = new Notification("Confirmation!", "Profile successfully updated!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			
			logger.log(Level.INFO, "The user was successfully updated!");
		}
		catch (Exception ex) {
			throw new UpdateUserException(UpdateUserExErrorType.DATABASE_FAILED);
		}
	}

}
