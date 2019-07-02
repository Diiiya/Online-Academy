package com.academy.onlineAcademy.helper;

import java.util.List;

import com.academy.onlineAcademy.controller.PersonController;
import com.academy.onlineAcademy.model.Person;
import com.vaadin.data.Binder;
import com.vaadin.server.Page;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;

public class UpdateUserMethods {
	
	public static boolean isSuccessful;
	
	public static void getUserInfo(int userId, Binder<Person> binder, PersonController personObj, Person selectedPerson) {
		try {
			selectedPerson = personObj.getPersonById(userId);			
			binder.readBean(selectedPerson);
			
		}
		catch(Exception ex) {
			Notification notif = new Notification("Warning", "Unexpected error!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
	}
	
	private static void updatePersonSettings(Person selectedPerson, Binder<Person> binder, PersonController personObj) {
		try {
			binder.writeBean(selectedPerson);
			isSuccessful = updateInDatabase(selectedPerson, personObj);
		}
		catch(Exception ex) {
			Notification notif = new Notification("Warning", "Please correct the fields in red!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
	}
	
	public static void existingEmail(Person selectedPerson, String enteredEmail, Binder<Person> binder, PersonController personObj) {
		try {
	    	selectedPerson = personObj.getPersonByEmail(enteredEmail.toUpperCase());
	    	if (enteredEmail.equals(selectedPerson.getEmail())) {
	    		updatePersonSettings(selectedPerson, binder, personObj);
	    	}
	    	else {
		    	Notification notif = new Notification("Warning", "The email is already used by another user!", Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
	    	}
	     }
		 catch (Exception ex) {
			 updatePersonSettings(selectedPerson, binder, personObj);
		 }
	}
	
	private static boolean updateInDatabase(Person selectedPerson, PersonController personObj) {
		try {
			personObj.updatePerson(selectedPerson);
			Notification notif = new Notification("Confirmation!", "Profile successfully updated!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			return true;
		}
		catch (Exception ex) {
			Notification notif = new Notification("Warning", "Unexpected error!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
		return false;
	}

}
