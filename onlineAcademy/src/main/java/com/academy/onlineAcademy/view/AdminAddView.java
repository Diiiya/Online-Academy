package com.academy.onlineAcademy.view;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.academy.onlineAcademy.controller.PersonController;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.academy.onlineAcademy.model.Person;
import com.academy.onlineAcademy.model.Type;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AdminAddView extends VerticalLayout implements View {
	
	public AdminAddView() {
		
        VerticalLayout mainVLayout = new VerticalLayout();
		
		// 1 - Header bar ?
		HorizontalLayout layoutH = new HorizontalLayout();
		layoutH.setSpacing(true);
		layoutH.setWidth("100%");
		layoutH.setHeight("90px");
		
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource logoResource = new FileResource(new File(basepath +
	            "/logo.jpg"));
			
		Image logoImage = new Image("", logoResource);
		logoImage.setWidth("130px");
		logoImage.setHeight("60px");
		Button myProfileButton = new Button("My profile", VaadinIcons.MENU);
		
		
		layoutH.addComponents(logoImage, myProfileButton);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(myProfileButton, Alignment.BOTTOM_RIGHT);
		
		// 2 - Add panel
		VerticalLayout layoutVBody = new VerticalLayout();
		layoutVBody.setWidth("100%");
		
		Panel panel = new Panel("Add a new user ... ");
		panel.setSizeUndefined();
		
		TextField fullNameField = new TextField("Full name:");
		TextField usernameField = new TextField("Username:");
		TextField emailField = new TextField("Email:");
		PasswordField passwordField = new PasswordField("Password: ");
		PasswordField confirmPasswordField = new PasswordField("Repeat password: ");
		
		List<String> types = Stream.of(Type.values())
                .map(Enum::name)
                .collect(Collectors.toList());
		
		ComboBox<String> selectTypeComboBox = new ComboBox<>("Select type:", types);
		// types -> index 2 is a USER type of Person
		selectTypeComboBox.setValue(types.get(2));
		selectTypeComboBox.setEmptySelectionAllowed(false);
		
		FormLayout content = new FormLayout();
		content.addComponents(fullNameField, usernameField, emailField, passwordField, confirmPasswordField, selectTypeComboBox);
		content.setSizeUndefined(); 
		content.setMargin(true);
		panel.setContent(content);
		
		///////// BINDER Part + validations
		Binder<Person> binder = new Binder<>();
		binder.forField(fullNameField).withValidator(new StringLengthValidator(
				"Name must be between 5 and 30 characters long!",3, 50))
		.asRequired("Cannot be empty")
	    .bind(Person::getFullName, Person::setFullName);
		
		binder.forField(usernameField).withValidator(new StringLengthValidator(
				"Username must be between 6 and 30 characters long!",3, 30))
		.asRequired("Cannot be empty")
	    .bind(Person::getUsername, Person::setUsername);
		
		binder.forField(emailField).withValidator(new EmailValidator(
			    "This doesn't seem to be a valid email address"))
		.withValidator(email -> email.length() <= 50, "Email address should be max 50 characters long!")
		.asRequired("Cannot be empty")
	    .bind(Person::getEmail, Person::setEmail);
		
		binder.forField(passwordField).asRequired("Cannot be empty")
		.withValidator(new RegexpValidator("Password should contain at least one digit, one lower, one upper case letter and special symbol (# $ ^ + = ! * () @ % &) "
				+ "and be at least 8 characters long!", "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$^+=!*()@%&]).{8,30}$"))
		.withValidator(new StringLengthValidator(
				"Password must be between 8 and 30 characters long!",8, 30))
		.bind(Person::getPassword, Person::setPassword);
		
		binder.forField(selectTypeComboBox)
		.asRequired("Cannot be empty");
		
		
		Button addButton = new Button("ADD");
		addButton.setWidth("100");
		addButton.addClickListener(e -> {
			if (fullNameField.getValue() != "" && usernameField.getValue() != "" && emailField.getValue() != "" && 
					passwordField.getValue() != "" && confirmPasswordField.getValue() != "") {
				PersonController obj = new PersonController();
				
				System.out.println("1: " + passwordField.getValue());
				System.out.println("2: " + confirmPasswordField.getValue());
				
				if (passwordField.getValue() == confirmPasswordField.getValue() ) {
					try {
						Type type = Type.valueOf(selectTypeComboBox.getValue());
						obj.addPerson(fullNameField.getValue(), usernameField.getValue(), emailField.getValue(), passwordField.getValue(), null, type, null, null);
						
						Notification notif = new Notification(
							    "Confirmation",
							    "The user has been created!",
							    Notification.TYPE_WARNING_MESSAGE);
						notif.show(Page.getCurrent());
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				else {
					Notification notif = new Notification(
						    "Warning",
						    "The fields for password and confrim password do not match!",
						    Notification.TYPE_WARNING_MESSAGE);
					notif.show(Page.getCurrent());
				}

			}
			else {
				Notification notif = new Notification(
					    "Warning",
					    "All required fields (*) should be filled in!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			} 
		});
		
		layoutVBody.addComponents(panel, addButton);
		layoutVBody.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		layoutVBody.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
		
        mainVLayout.addComponents(layoutH, layoutVBody);
        addComponent(mainVLayout);
	}

}
