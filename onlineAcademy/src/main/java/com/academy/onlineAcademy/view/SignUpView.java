package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.academy.onlineAcademy.controller.PersonController;
import com.academy.onlineAcademy.helper.NewUserMethods;
import com.academy.onlineAcademy.model.Person;
import com.academy.onlineAcademy.model.Type;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class SignUpView extends VerticalLayout implements View {
	
	Navigator navigator = UI.getCurrent().getNavigator();
	private Binder<Person> binder = new Binder<>();
	private final TextField fullNameField = new TextField("Full name:");
	private final TextField usernameField = new TextField("Username:");
	private final TextField emailField = new TextField("Email:");
	private final PasswordField passwordField = new PasswordField("Password: ");
	private final PasswordField confirmPasswordField = new PasswordField("Repeat password: ");
	
	public SignUpView() {
		
		initMainLayout();
		
	}
	
	public void initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		
		HorizontalLayout layoutH = getTopBar();
		VerticalLayout layoutVBody = getLayoutBody();
		callBinder();
		
		mainVLayout.addComponents(layoutH, layoutVBody);
	    addComponent(mainVLayout);		
	}
	
    public HorizontalLayout getTopBar() {
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
		
		layoutH.addComponents(logoImage);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		
		return layoutH;
    }
    
    public VerticalLayout getLayoutBody() {
    	VerticalLayout layoutVBody = new VerticalLayout();
    	layoutVBody.setWidth("100%");
    	
		Panel panel = new Panel("Create account ... ");
		panel.setSizeUndefined();
        FormLayout content = new FormLayout();
        content.addComponents(fullNameField, usernameField, emailField, passwordField, confirmPasswordField);
		content.setSizeUndefined(); 
		content.setMargin(true);
		panel.setContent(content);
		
		Button addButton = new Button("ADD");
		
		addButton.setWidth("100");
		addButton.addClickListener(e -> {
			NewUserMethods.setFieldsValues(binder, Type.USER, fullNameField.getValue(), usernameField.getValue(), emailField.getValue(),
					passwordField.getValue(), confirmPasswordField.getValue()); 
		});
		
		
		layoutVBody.addComponents(panel, addButton);
		layoutVBody.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		layoutVBody.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
				
        return layoutVBody;
    }
    
    public Binder<Person> callBinder() {
    	
		///////// BINDER Part + validations
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
		.bind(Person::getPassword, Person::setPassword);
		binder.forField(confirmPasswordField).asRequired("Cannot be empty");
		
		return binder;
		
    }
	
}
