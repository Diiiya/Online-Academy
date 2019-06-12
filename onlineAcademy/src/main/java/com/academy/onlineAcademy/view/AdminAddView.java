package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.academy.onlineAcademy.model.Person;
import com.academy.onlineAcademy.model.Type;
import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
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
		FileResource resource = new FileResource(new File(basepath +
	            "/user1.png"));
			
		Image logoImage = new Image("", logoResource);
		logoImage.setWidth("130px");
		logoImage.setHeight("60px");
		Image image = new Image("", resource);
		image.setWidth("50px");
		image.setHeight("50px");
		
		layoutH.addComponents(logoImage, image);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(image, Alignment.TOP_RIGHT);
		
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
		
		List<String> types = new ArrayList<>();
		types.add("USER");
		types.add("TEACHER");
		types.add("ADMIN");
		ComboBox<String> selectComboBox = new ComboBox<>("Select type of user:", types);
		selectComboBox.setValue("USER");
		selectComboBox.setEmptySelectionAllowed(false);
		
		FormLayout content = new FormLayout();
		content.addComponents(fullNameField, usernameField, emailField, passwordField, confirmPasswordField, selectComboBox);
		content.setSizeUndefined(); 
		content.setMargin(true);
		panel.setContent(content);
		
		Button addButton = new Button("ADD");
		addButton.setWidth("100");
		
		layoutVBody.addComponents(panel, addButton);
		layoutVBody.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		layoutVBody.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
		
        mainVLayout.addComponents(layoutH, layoutVBody);
        addComponent(mainVLayout);
	}

}
