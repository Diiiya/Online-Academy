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
	
	private String value;
	String databaseUsername;
	String enteredUsername;
	String databasePassword;
	String enteredPassword;
	PersonController obj = new PersonController();
	Person currentUser = new Person();
	
	final VerticalLayout layout = new VerticalLayout();
			String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
			FileResource resource = new FileResource(new File(basepath +
		            "/1024px-Circle-icons-profile.svg.png"));
			Image image = new Image("", resource);
			TextField usernameField = new TextField("Type your username:");
			PasswordField passwordField = new PasswordField("Type your password:");
			Button loginButton = new Button("Login");
			Button signUpButton = new Button("Sign up");
			Navigator navigator = UI.getCurrent().getNavigator();
	
	public LoginView() {
		
		image.setWidth("100px");
		image.setHeight("100px");
	       
	    usernameField.setWidth("200px");
	    passwordField.setWidth("200px");
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
	
	public void checkIfTheUsernameExists() {
		try {		    	
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
    		navigator.navigateTo("UserCourses" + "/" + currentUser.getId());
    		
    		UI ui = UI.getCurrent();
    		VaadinSession session = ui.getSession();
    		session.setAttribute("user-id", currentUser.getId());
    		System.out.println("The value in the first method: " + value);
    		
//    		UI ui = UI.getCurrent();
//    		String value = String.valueOf(currentUser.getId());
//    		saveValue(this, value);
//    		System.out.println("The value in the first method: " + value);

    	}
    	else {
    		Notification notif = new Notification("Warning", "The combination of username and password is not correct!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
    	}
	}
	
//    private static void saveValue(LoginView ui,
//            String value) {
//        // Save to UI instance
//       // ui.value = value;
//        System.out.println("The value in the second method: " + value);
//        // Save to VaadinServiceSession
//        ui.getSession().setAttribute("user-id", value);
//        // Save to HttpSession
//       // VaadinService.getCurrentRequest().getWrappedSession()
//               // .setAttribute("user-id", value);
//
//    }
	
}
