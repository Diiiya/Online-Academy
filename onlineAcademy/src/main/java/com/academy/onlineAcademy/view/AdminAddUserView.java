package com.academy.onlineAcademy.view;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.academy.onlineAcademy.controller.PersonController;
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
	
	Navigator navigator = UI.getCurrent().getNavigator();
	public static Binder<Person> binder = new Binder<>();
	
	VerticalLayout mainVLayout = new VerticalLayout();	
			HorizontalLayout layoutH = new HorizontalLayout();
					String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
					FileResource logoResource = new FileResource(new File(basepath +
				            "/logo.jpg"));
					Image logoImage = new Image("", logoResource);
					MenuBar profileMenu = new MenuBar();
			VerticalLayout layoutVBody = new VerticalLayout();
					Panel panel = new Panel("Add a new user ... ");
							FormLayout content = new FormLayout();
									TextField fullNameField = new TextField("Full name:");
									TextField usernameField = new TextField("Username:");
									TextField emailField = new TextField("Email:");
									PasswordField passwordField = new PasswordField("Password: ");
									PasswordField confirmPasswordField = new PasswordField("Repeat password: ");
									List<String> types = Stream.of(Type.values())
								            .map(Enum::name)
								            .collect(Collectors.toList());
									ComboBox<String> selectTypeComboBox = new ComboBox<>("Select type:", types);
							Button addButton = new Button("ADD");
	
	public AdminAddUserView() {
		
		// 1 - Header bar and UI settings
		layoutH.setSpacing(true);
		layoutH.setWidth("100%");
		layoutH.setHeight("90px");
		
		logoImage.setWidth("130px");
		logoImage.setHeight("60px");
	
		// MENU bar and methods to navigate to different pages
		MenuItem myProfileMainItem = profileMenu.addItem("My profile", VaadinIcons.MENU, null);
		MenuItem allCoursesItem = myProfileMainItem.addItem("All courses", VaadinIcons.ACADEMY_CAP, createNavigationCommand("AdminAllCourses"));
		MenuItem addCoursesItem = myProfileMainItem.addItem("Add course", VaadinIcons.FILE_ADD, createNavigationCommand("AdminAddCourse"));
		MenuItem allOrdersItem = myProfileMainItem.addItem("All orders", VaadinIcons.NEWSPAPER, createNavigationCommand("AdminAllOrders"));
		MenuItem allUsersItem = myProfileMainItem.addItem("All users", VaadinIcons.USERS, createNavigationCommand("AdminAllUsers"));
		MenuItem addUserItem = myProfileMainItem.addItem("Add user", VaadinIcons.PLUS, createNavigationCommand("AdminAddUser"));
		MenuItem mySettingsItem = myProfileMainItem.addItem("Settings", VaadinIcons.USER, createNavigationCommand("Settings"));
		MenuItem myLogoutItem = myProfileMainItem.addItem("Log out", VaadinIcons.EXIT, createNavigationCommand("Home"));
		
		layoutH.addComponents(logoImage, profileMenu);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(profileMenu, Alignment.BOTTOM_RIGHT);
		
		// 2 - Add panel
		layoutVBody.setWidth("100%");
		panel.setSizeUndefined();
		selectTypeComboBox.setValue(types.get(2));
		selectTypeComboBox.setEmptySelectionAllowed(false);
		
		content.addComponents(fullNameField, usernameField, emailField, passwordField, confirmPasswordField, selectTypeComboBox);
		content.setSizeUndefined(); 
		content.setMargin(true);
		panel.setContent(content);
		
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
		
		
		addButton.setWidth("100");
		addButton.addClickListener(e -> NewUserMethods.checkEmptyFields(1, fullNameField.getValue(), usernameField.getValue(), emailField.getValue(),
				passwordField.getValue(), confirmPasswordField.getValue()));
		
		layoutVBody.addComponents(panel, addButton);
		layoutVBody.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		layoutVBody.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
		
        mainVLayout.addComponents(layoutH, layoutVBody);
        addComponent(mainVLayout);
	}
	
	MenuBar.Command createNavigationCommand(String navigationView) {
		return new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
		    	navigator.navigateTo(navigationView);
		    }
		};
	}

}
