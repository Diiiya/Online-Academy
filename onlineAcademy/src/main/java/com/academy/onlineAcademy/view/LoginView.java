package com.academy.onlineAcademy.view;

import java.io.File;

import com.academy.onlineAcademy.controller.PersonController;
import com.academy.onlineAcademy.model.Person;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class LoginView extends VerticalLayout implements View {
	
	private Navigator navigator;
	private String enteredUsername;
	private String enteredPassword;
	private Person currentUser;
	
	private final TextField usernameField = new TextField("Type your username:");
	private final PasswordField passwordField = new PasswordField("Type your password:");
	
	public LoginView() {
		
		navigator = UI.getCurrent().getNavigator();
		currentUser = new Person();
		initMainLayout();
		
	}			
			
    public VerticalLayout initMainLayout() {
    	VerticalLayout layout = new VerticalLayout();
    	
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource resource = new FileResource(new File(basepath + "/1024px-Circle-icons-profile.svg.png"));
		Image image = new Image("", resource);
		image.setWidth("100px");
		image.setHeight("100px");
		
		usernameField.setWidth("200px");
	    passwordField.setWidth("200px");
	    Button signUpButton = new Button("Sign up");
	    signUpButton.setWidth("200px");
	    signUpButton.addClickListener(e -> navigator.navigateTo("SignUp"));
	    
	    Button loginButton = getLoginButton();
	    
        layout.addComponents(image, usernameField, passwordField, loginButton, signUpButton);
	    
	    layout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
	    layout.setComponentAlignment(usernameField, Alignment.MIDDLE_CENTER);
	    layout.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);
	    layout.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
	    layout.setComponentAlignment(signUpButton, Alignment.MIDDLE_CENTER);
	    
	    addComponent(layout);
	    
	    return layout;
    }
    
    public Button getLoginButton() {
    	Button loginButton = new Button("Login");
    	loginButton.setWidth("200px");
 	    
 	    loginButton.addClickListener(e -> {
 	    	enteredUsername = usernameField.getValue().toUpperCase();
 		    enteredPassword = passwordField.getValue();
 		    if (((enteredUsername.length() != 0) && (enteredUsername != "")) && ((passwordField.getValue().length() != 0) && (passwordField.getValue() != ""))) {
 		          checkIfTheUsernameExists();
 		          checkUsernamePasswordCombination();
 		    }
 		    else {
 		    	Notification notif = new Notification("Warning", "Both fields should be filled in!",
 					    Notification.TYPE_WARNING_MESSAGE);
 		    	notif.show(Page.getCurrent());
 		    }
 	    });
 	    
 	    return loginButton;
    }
	
	public void checkIfTheUsernameExists() {
		try {
			PersonController obj = new PersonController();
	    	currentUser = obj.getPersonByUsername(enteredUsername);
	     }
		 catch (Exception ex) {
			 Notification notif = new Notification("Warning", "The username doesn't exist! Please use another one or create an account!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
		 }
	}
	
	public void checkUsernamePasswordCombination() {
		if (currentUser.getPassword().equals(enteredPassword)) {
			checkTypeOfUser();
    	}
    	else {
    		Notification notif = new Notification("Warning", "The combination of username and password is not correct!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
    	}
	}
	
	public void checkTypeOfUser() {
		UI ui = UI.getCurrent();
		VaadinSession session = ui.getSession();
		if (currentUser.getType().toString() == "USER") {
            navigator.navigateTo("UserCourses" + "/" + currentUser.getId());
    		session.setAttribute("user-id", currentUser.getId());
		}
		else if (currentUser.getType().toString() == "TEACHER") {
			
		}
		else if (currentUser.getType().toString() == "ADMIN") {
            navigator.navigateTo("AdminAllCourses" + "/" + currentUser.getId());
    		session.setAttribute("user-id", currentUser.getId());
		}
		else {
			Notification notif = new Notification("Warning", "Unexpected error! Please try again!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
	}

	
}
