package com.academy.onlineAcademy.view;

import java.io.File;

import com.academy.onlineAcademy.controller.PersonController;
import com.academy.onlineAcademy.helpView.UserViews;
import com.academy.onlineAcademy.helper.UpdateUserMethods;
import com.academy.onlineAcademy.model.Person;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class UserSettingsView extends VerticalLayout implements View {
	
	private Navigator navigator;
	private Binder<Person> binder;
	private PersonController personObj;
	private Person person;
	private int userId;		
	
	private Panel photoPanel;	
	private Panel accountPanel;	
	private TextField fullNameField;
	private TextField emailField;
	private PasswordField passwordField;
	private PasswordField confirmPasswordField;
		
	public UserSettingsView() {
		
		navigator = UI.getCurrent().getNavigator();
		person = new Person();
		personObj = new PersonController();
		getMainLayout();
				
	}
    
	public VerticalLayout getMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		
		HorizontalLayout layoutH = UserViews.getTopBar(navigator);
		VerticalLayout layoutVBody = getBodyLayout();
		
		mainVLayout.addComponents(layoutH, layoutVBody);
		addComponents(mainVLayout);
		
		return mainVLayout;
	}
					
	public VerticalLayout getBodyLayout() {
		VerticalLayout layoutVBody = new VerticalLayout();		
    	layoutVBody.setWidth("100%");
    	HorizontalLayout layoutHBody = new HorizontalLayout();
		layoutHBody.setSizeUndefined();
		getPhotoPanel();
		getAccountpanel();
		callBinder();
		Button updateButton = new Button("UPDATE");
		updateButton.setWidth("800");
		updateButton.addClickListener(e -> {
			String enteredEmail = emailField.getValue();
			UpdateUserMethods.existingEmail(person, enteredEmail, binder, personObj);
			});
		
		layoutHBody.addComponents(photoPanel, accountPanel);
		layoutHBody.setComponentAlignment(accountPanel, Alignment.MIDDLE_CENTER);

		layoutVBody.addComponents(layoutHBody, updateButton);
		layoutVBody.setComponentAlignment(layoutHBody, Alignment.MIDDLE_CENTER);
		layoutVBody.setComponentAlignment(updateButton, Alignment.MIDDLE_CENTER);
		
		return layoutVBody;
    }
	
	public Panel getPhotoPanel() {
		photoPanel = new Panel("Update my photo");
		FormLayout photoContent = new FormLayout();
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();	
		FileResource profileImageResource = new FileResource(new File(basepath + "/1024px-Circle-icons-profile.svg.png"));
		Image profileImage = new Image("", profileImageResource);
		HorizontalLayout layoutHsmall = new HorizontalLayout();
		TextField photoLabel = new TextField("Photo");
		Button photoAddButton = new Button("Add image");
		
		photoPanel.setSizeUndefined();
		photoPanel.setHeight("400px");
		profileImage.setWidth("200px");
		profileImage.setHeight("200px");
		
		photoContent.addComponent(profileImage);
		photoContent.setComponentAlignment(profileImage, Alignment.MIDDLE_CENTER);
		layoutHsmall.addComponents(photoLabel, photoAddButton);
		layoutHsmall.setComponentAlignment(photoAddButton, Alignment.BOTTOM_RIGHT);
		photoContent.addComponent(layoutHsmall);
		
		photoContent.setSizeUndefined(); 
		photoContent.setMargin(true);
		photoPanel.setContent(photoContent);
		
		return photoPanel;
	}
	
	public Panel getAccountpanel() {
		accountPanel = new Panel("Update my account");
		fullNameField = new TextField("Full name:");
		emailField = new TextField("Email:");
		passwordField = new PasswordField("Password:");
		confirmPasswordField = new PasswordField("Confirm password");
		FormLayout content = new FormLayout();
		
		accountPanel.setSizeUndefined();
		accountPanel.setHeight("400px");
		
		content.addComponents(fullNameField, emailField, passwordField, confirmPasswordField);
		content.setSizeUndefined(); 
		content.setMargin(true);
		accountPanel.setContent(content);
		
		return accountPanel;
	}
	
	public void callBinder() {
		binder = new Binder<Person>();
		
		binder.forField(fullNameField)
		.withValidator(new StringLengthValidator("Name must be between 5 and 30 characters long!",3, 50))
		.asRequired("Cannot be empty")
	    .bind(Person::getFullName, Person::setFullName);
		
		binder.forField(emailField)
		.withValidator(new EmailValidator("This doesn't seem to be a valid email address"))
		.withValidator(email -> email.length() <= 50, "Email address should be max 50 characters long!")
		.asRequired("Cannot be empty")
	    .bind(Person::getEmail, Person::setEmail);
		
		binder.forField(passwordField).asRequired("Cannot be empty")
		.withValidator(new RegexpValidator("Password should contain at least one digit, one lower, one upper case letter and special symbol (# $ ^ + = ! * () @ % &) "
				+ "and be at least 8 characters long!", "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$^+=!*()@%&]).{8,30}$"))
		.bind(Person::getPassword, Person::setPassword);
		//binder.forField(confirmPasswordField).asRequired("Cannot be empty");
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		UI ui = UI.getCurrent();
		VaadinSession session = ui.getSession();
		if (session.getAttribute("user-id") != null) {
			int userId = Integer.valueOf(String.valueOf(session.getAttribute("user-id")));
			UpdateUserMethods.getUserInfo(userId, binder, personObj, person);
		}
		else {
			System.out.println("USER ID VAL:" + session.getAttribute("user-id"));
		}
	}
	
}
