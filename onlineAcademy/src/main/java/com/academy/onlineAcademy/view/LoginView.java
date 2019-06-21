package com.academy.onlineAcademy.view;

import java.io.File;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class LoginView extends VerticalLayout implements View {
	
	final VerticalLayout layout = new VerticalLayout();
	
	final TextField usernameField = new TextField();
	final TextField passwordField = new TextField();
	Button loginButton = new Button("Login");
	Button signUpButton = new Button("Sign up");
	Navigator navigator = UI.getCurrent().getNavigator();
	
	public LoginView() {
		
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource resource = new FileResource(new File(basepath +
	            "/1024px-Circle-icons-profile.svg.png"));
			
		Image image = new Image("", resource);
		image.setWidth("100px");
		image.setHeight("100px");
	    
	    usernameField.setCaption("Type your username:");    
	    usernameField.setWidth("200px");
	    passwordField.setCaption("Type your password:");
	    passwordField.setWidth("200px");
	    loginButton.setWidth("200px");
	    loginButton.addClickListener(e -> {
	        // 1. Checks user name and password combination
	    	// 2. If true -> navigates to the user's (with id) courses page
	    	navigator.navigateTo("UserCourses");
	    	// 3. If false -> displays a wrong input combination message
	    });
	    signUpButton.setWidth("200px");
	    signUpButton.addClickListener(e -> navigator.navigateTo("SignUp"));
	    
	    layout.addComponents(image, usernameField, passwordField, loginButton, signUpButton);
	    
	    layout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
	    layout.setComponentAlignment(usernameField, Alignment.MIDDLE_CENTER);
	    layout.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);
	    layout.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
	    layout.setComponentAlignment(signUpButton, Alignment.MIDDLE_CENTER);
	    
	    addComponent(layout);
		
	}

}
