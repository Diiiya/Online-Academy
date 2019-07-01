package com.academy.onlineAcademy.view;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.academy.onlineAcademy.controller.PersonController;
import com.academy.onlineAcademy.helpView.AdminViews;
import com.academy.onlineAcademy.helper.NewUserMethods;
import com.academy.onlineAcademy.model.Person;
import com.academy.onlineAcademy.model.Type;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
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
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.MenuItem;

public class AdminAddUserView extends VerticalLayout implements View {
	
	private Navigator navigator = UI.getCurrent().getNavigator();
	private Binder<Person> binder = new Binder<>();
	
	private final TextField fullNameField = new TextField("Full name:");
	private final TextField usernameField = new TextField("Username:");
	private final TextField emailField = new TextField("Email:");
	private final PasswordField passwordField = new PasswordField("Password: ");
	private final PasswordField confirmPasswordField = new PasswordField("Repeat password: ");
	private final List<String> types = Stream.of(Type.values())
            .map(Enum::name)
            .collect(Collectors.toList());
	private final ComboBox<String> selectTypeComboBox = new ComboBox<>("Select type:", types);
	
	public AdminAddUserView() {
		
		initMainLayout();
		
	}
	
	public VerticalLayout initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		
		HorizontalLayout layoutH = AdminViews.getTopBar(navigator);
		VerticalLayout layoutVBody = getBodyLayout();
		callBinder();
		
		mainVLayout.addComponents(layoutH, layoutVBody);
        addComponent(mainVLayout);
        
        return mainVLayout;
	}
	
	public VerticalLayout getBodyLayout() {
		VerticalLayout layoutVBody = new VerticalLayout();
		layoutVBody.setWidth("100%");
		
		Panel panel = new Panel("Add a new user ... ");
		panel.setSizeUndefined();
		selectTypeComboBox.setValue(types.get(2));
		selectTypeComboBox.setEmptySelectionAllowed(false);
		
		FormLayout content = new FormLayout();
		content.addComponents(fullNameField, usernameField, emailField, passwordField, confirmPasswordField, selectTypeComboBox);
		content.setSizeUndefined(); 
		content.setMargin(true);
		panel.setContent(content);
		
		Button addButton = getAddButton();
		layoutVBody.addComponents(panel, addButton);
		layoutVBody.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		layoutVBody.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
		
		return layoutVBody;
	}
	
	public Button getAddButton() {
		Button addButton = new Button("ADD");
		addButton.setWidth("100");
		addButton.addClickListener(e -> NewUserMethods.setFieldsValues(binder, Type.ADMIN, fullNameField.getValue(), usernameField.getValue(), emailField.getValue(),
				passwordField.getValue(), confirmPasswordField.getValue()));
		
		return addButton;
	}
	
	public void callBinder() {
		///////// BINDER Part + validations
		binder.forField(fullNameField)
		.withValidator(new StringLengthValidator("Name must be between 5 and 30 characters long!",3, 50))
		.withValidator(string -> string != null && !string.isEmpty(), "Input values should not be empty")
		.asRequired("Cannot be empty")
	    .bind(Person::getFullName, Person::setFullName);
		
		binder.forField(usernameField)
		.withValidator(new StringLengthValidator("Username must be between 6 and 30 characters long!",3, 30))
		.withValidator(string -> string != null && !string.isEmpty(), "Input values should not be empty")
		.asRequired("Cannot be empty")
	    .bind(Person::getUsername, Person::setUsername);
		
		binder.forField(emailField)
		.withValidator(new EmailValidator("This doesn't seem to be a valid email address"))
		.withValidator(email -> email.length() <= 50, "Email address should be max 50 characters long!")
		.withValidator(string -> string != null && !string.isEmpty(), "Input values should not be empty")
		.asRequired("Cannot be empty")
	    .bind(Person::getEmail, Person::setEmail);
		
		binder.forField(passwordField).asRequired("Cannot be empty")
		.withValidator(new RegexpValidator("Password should contain at least one digit, one lower, one upper case letter and special symbol (# $ ^ + = ! * () @ % &) "
				+ "and be at least 8 characters long!", "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$^+=!*()@%&]).{8,30}$"))
		.withValidator(string -> string != null && !string.isEmpty(), "Input values should not be empty")
		.bind(Person::getPassword, Person::setPassword);
		binder.forField(confirmPasswordField).asRequired("Cannot be empty");
		
	}

}
