package com.academy.onlineAcademy.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.controller.PersonController;
import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.academy.onlineAcademy.model.Person;
import com.academy.onlineAcademy.model.Type;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.MenuBar.MenuItem;

public class AdminAllUsersView extends VerticalLayout implements View {
	
	Navigator navigator = UI.getCurrent().getNavigator();
	PersonController personObj = new PersonController();
	List<Person> personas;
	Grid<com.academy.onlineAcademy.model.Person> grid = new Grid<>();
	Person selectedPerson;
	int selectedPersonId;
	
	public AdminAllUsersView() {
		VerticalLayout mainVLayout = new VerticalLayout();
		mainVLayout.setHeight("100%");
		
		// 1 - Header bar ?
		HorizontalLayout layoutH = new HorizontalLayout();
		layoutH.setSpacing(true);
		layoutH.setWidth("100%");
		layoutH.setHeight("70px");
		
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource logoResource = new FileResource(new File(basepath +
	            "/logo.jpg"));
			
		Image logoImage = new Image("", logoResource);
		logoImage.setWidth("130px");
		logoImage.setHeight("60px");
		
		// MENU bar and methods to navigate to different pages
		MenuBar profileMenu = new MenuBar();
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
		
		// 2 - Search
		
		HorizontalLayout searchHLayout = new HorizontalLayout();
		TextField searchField = new TextField("");
		searchField.setPlaceholder("SEARCH");
		Button searchButton = new Button("Search", VaadinIcons.SEARCH);
		searchButton.addClickListener(e -> {
			Person selectedPerson = personObj.getPersonById(Integer.parseInt(searchField.getValue()));
			grid.setItems(selectedPerson);
		});
		searchHLayout.addComponents(searchField, searchButton);
		searchHLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);
		
		// 3 - All users:
		Label topLabel = new Label("Seach for a specific user:");		
		personas = personObj.getAllUsers();
		
		grid.setItems(personas);
		
		
		grid.addColumn(com.academy.onlineAcademy.model.Person::getId).setCaption("Id");
		grid.addColumn(com.academy.onlineAcademy.model.Person::getFullName).setCaption("Full name");
		grid.addColumn(com.academy.onlineAcademy.model.Person::getUsername).setCaption("Username");
		grid.addColumn(com.academy.onlineAcademy.model.Person::getEmail).setCaption("Email");
		grid.addColumn(com.academy.onlineAcademy.model.Person::getType).setCaption("Type");
		grid.addColumn(com.academy.onlineAcademy.model.Person::getPassword).setCaption("Password");
		
		grid.setWidth("100%");
		grid.setHeight("100%");
		
		HorizontalLayout buttonsHLayout = new HorizontalLayout();
		buttonsHLayout.setVisible(false);
		Button deleteCourseButton = new Button("Delete", VaadinIcons.DEL);
		deleteCourseButton.setWidth("200px");
		Button updateCourseButton = new Button("Update", VaadinIcons.REFRESH);
		updateCourseButton.setWidth("200px");
		buttonsHLayout.addComponents(updateCourseButton, deleteCourseButton);
		
		grid.addItemClickListener(e -> {
			selectedPerson = e.getItem();
			selectedPersonId = selectedPerson.getId();
			System.out.println(selectedPersonId);
			buttonsHLayout.setVisible(true);
		});

		deleteCourseButton.addClickListener(e -> {
			try {
			personObj.deleteCourseById(selectedPersonId);
	        personas = personObj.getAllUsers();
	        grid.setItems(personas);
	        buttonsHLayout.setVisible(false);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		});
		
		TextField fullNameField = new TextField("Full name:");
		TextField usernameField = new TextField("Username:");
		TextField emailField = new TextField("Email:");
		PasswordField passwordField = new PasswordField("Password:");
		
		List<String> types = Stream.of(Type.values())
                .map(Enum::name)
                .collect(Collectors.toList());
		
		ComboBox<String> selectTypeComboBox = new ComboBox<>("Select type:", types);
		selectTypeComboBox.setEmptySelectionAllowed(false);
		
		Button updateFormButton = new Button("Update", VaadinIcons.REFRESH);
		
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
		.bind(Person::getPassword, Person::setPassword);
		binder.forField(selectTypeComboBox)
		.asRequired("Cannot be empty");
		
		FormLayout content = new FormLayout();
		content.addComponents(fullNameField, usernameField, emailField, passwordField, selectTypeComboBox, updateFormButton);
		content.setSizeUndefined(); 
		content.setMargin(true);
		
		Window updateWindow = new Window("UPDATE USER");
		updateWindow.setVisible(false);
		updateCourseButton.addClickListener(e -> {
			updateWindow.setVisible(true);
			updateWindow.center();
			updateWindow.setContent(content);
			if (selectedPerson != null) {
			fullNameField.setValue(selectedPerson.getFullName());
			usernameField.setValue(selectedPerson.getUsername());
			emailField.setValue(String.valueOf(selectedPerson.getEmail()));
			passwordField.setValue(selectedPerson.getPassword());
			selectTypeComboBox.setValue(selectedPerson.getType().toString());
			UI.getCurrent().addWindow(updateWindow);
			}
		});
		
		updateFormButton.addClickListener(e -> {
			if (fullNameField.getValue() != "" && usernameField.getValue() != "" && emailField.getValue() != "" && passwordField.getValue() != "") {
				PersonController obj = new PersonController();
				
				try {
					
					// Getting and converting String values from the UI to the required values for the Course constructor
					Type type = Type.valueOf(selectTypeComboBox.getValue());
					obj.updatePersonById(selectedPerson, selectedPerson.getId(), fullNameField.getValue(), 
							usernameField.getValue(), emailField.getValue(), passwordField.getValue(), type);
					
					Notification notif = new Notification(
						    "Confirmation",
						    "The user has been updated!",
						    Notification.TYPE_WARNING_MESSAGE);
					notif.show(Page.getCurrent());
					
			        personas = personObj.getAllUsers();
			        grid.setItems(personas);
			        
			        updateWindow.setVisible(false);
			        buttonsHLayout.setVisible(false);
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				else {
					Notification notif = new Notification(
						    "Warning",
						    "All required fields should be filled in!",
						    Notification.TYPE_WARNING_MESSAGE);
					notif.show(Page.getCurrent());
				}
		});
		
		mainVLayout.addComponents(layoutH, searchHLayout, topLabel, grid, buttonsHLayout);
		mainVLayout.setComponentAlignment(searchHLayout, Alignment.MIDDLE_CENTER);
		mainVLayout.setComponentAlignment(buttonsHLayout, Alignment.BOTTOM_CENTER);
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
