package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.academy.onlineAcademy.controller.PersonController;
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
	private String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	Binder<Person> binder = new Binder<>();
	Person person = new Person();
	
	private final TextField fullNameField = new TextField("Full name:");
	private final TextField usernameField = new TextField("Username:");
	private final TextField emailField = new TextField("Email:");
	private final PasswordField passwordField = new PasswordField("Password:");	
	private final List<String> types = Stream.of(Type.values())
            .map(Enum::name)
            .collect(Collectors.toList());	
	private final ComboBox<String> selectTypeComboBox = new ComboBox<>("Select type:", types);
	
	private HorizontalLayout buttonsHLayout;
	private Window updateWindow;
	
	private PersonController personObj = new PersonController();
	private List<Person> personas;
	private Grid<com.academy.onlineAcademy.model.Person> grid = new Grid<>();
	private Person selectedPerson;
	private int selectedPersonId;
	
	public AdminAllUsersView() {
		
		initMainLayout();
		
	}
	
	public VerticalLayout initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		mainVLayout.setHeight("100%");
		
		HorizontalLayout layoutH = getTopBar();
		HorizontalLayout searchHLayout = getSearchLayout();
		selectTypeComboBox.setEmptySelectionAllowed(false);
		Label topLabel = new Label("Seach for a specific user:");		
		grid = getGrid();
		buttonsHLayout = buttonsDELIUPDLayout();
		callBinder();
		callUpdateWindow();
		
		mainVLayout.addComponents(layoutH, searchHLayout, topLabel, grid, buttonsHLayout);
		mainVLayout.setComponentAlignment(searchHLayout, Alignment.MIDDLE_CENTER);
		mainVLayout.setComponentAlignment(buttonsHLayout, Alignment.BOTTOM_CENTER);
		addComponent(mainVLayout);
		
		return mainVLayout;
	}
	
	public HorizontalLayout getTopBar() {
		
		HorizontalLayout layoutH = new HorizontalLayout();
		layoutH.setSpacing(true);
		layoutH.setWidth("100%");
		layoutH.setHeight("90px");
		
		FileResource logoResource = new FileResource(new File(basepath +
	            "/logo.jpg"));
		Image logoImage = new Image("", logoResource);
		logoImage.setWidth("130px");
		logoImage.setHeight("60px");
		
		// MENU bar and methods to navigate to different pages
		MenuBar profileMenu = new MenuBar();
		MenuItem myProfileMainItem = profileMenu.addItem("My profile", VaadinIcons.MENU, null);
		myProfileMainItem.addItem("All courses", VaadinIcons.ACADEMY_CAP, createNavigationCommand("AdminAllCourses"));
		myProfileMainItem.addItem("Add course", VaadinIcons.FILE_ADD, createNavigationCommand("AdminAddCourse"));
		myProfileMainItem.addItem("All orders", VaadinIcons.NEWSPAPER, createNavigationCommand("AdminAllOrders"));
		myProfileMainItem.addItem("All users", VaadinIcons.USERS, createNavigationCommand("AdminAllUsers"));
		myProfileMainItem.addItem("Add user", VaadinIcons.PLUS, createNavigationCommand("AdminAddUser"));
		myProfileMainItem.addItem("Settings", VaadinIcons.USER, createNavigationCommand("Settings"));
		myProfileMainItem.addItem("Log out", VaadinIcons.EXIT, createNavigationCommand("Home"));
		
		layoutH.addComponents(logoImage, profileMenu);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(profileMenu, Alignment.BOTTOM_RIGHT);
		
		return layoutH;
	}
	
	public HorizontalLayout getSearchLayout() {
		HorizontalLayout searchHLayout = new HorizontalLayout();
		
		TextField searchField = new TextField("");
		searchField.setPlaceholder("SEARCH");
		Button searchButton = new Button("Search", VaadinIcons.SEARCH);
		searchButton.addClickListener(e -> {
			try {
				Person selectedPerson = personObj.getPersonById(Integer.parseInt(searchField.getValue()));
				grid.setItems(selectedPerson);
			}
			catch(Exception ex) {
				Notification notif = new Notification("Warning", "No user with this id has been found!", Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			}			
		});
		searchHLayout.addComponents(searchField, searchButton);
		searchHLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);
		
		return searchHLayout;
	}
	
	public HorizontalLayout buttonsDELIUPDLayout() {
		buttonsHLayout = new HorizontalLayout();
		buttonsHLayout.setVisible(false);
		
		Button deleteUserButton = new Button("Delete", VaadinIcons.DEL);
		deleteUserButton.setWidth("200px");
		Button updateUserButton = new Button("Update", VaadinIcons.REFRESH);
		updateUserButton.setWidth("200px");
		buttonsHLayout.addComponents(updateUserButton, deleteUserButton);
		
		updateUserButton.addClickListener(e -> {
			getUserInfo(selectedPersonId);
		});
		
		deleteUserButton.addClickListener(e -> {
			try {
				personObj.deletePersonById(selectedPersonId);
		        personas = personObj.getAllUsers();
		        grid.setItems(personas);
		        buttonsHLayout.setVisible(false);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		});
		
		return buttonsHLayout;
	}
	
	public Grid<Person> getGrid() {
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
		
		grid.addItemClickListener(e -> {
			selectedPerson = e.getItem();
			selectedPersonId = selectedPerson.getId();
			buttonsHLayout.setVisible(true);
		});
		
		return grid;
	}
	
	public void callBinder() {
		
		binder.forField(fullNameField).withValidator(new StringLengthValidator(
				"Name must be between 5 and 30 characters long!",3, 50))
		.asRequired("Cannot be empty")
	    .bind(Person::getFullName, Person::setFullName);
		
//		binder.forField(usernameField).withValidator(new StringLengthValidator(
//				"Username must be between 6 and 30 characters long!",3, 30))
//		.asRequired("Cannot be empty")
//	    .bind(Person::getUsername, Person::setUsername);
		
		binder.forField(emailField).withValidator(new EmailValidator(
			    "This doesn't seem to be a valid email address"))
		.withValidator(email -> email.length() <= 50, "Email address should be max 50 characters long!")
		.asRequired("Cannot be empty")
	    .bind(Person::getEmail, Person::setEmail);
		
		binder.forField(passwordField).asRequired("Cannot be empty")
		.bind(Person::getPassword, Person::setPassword);
		binder.forField(selectTypeComboBox).asRequired("Cannot be empty")
		.withConverter(Type::valueOf, String::valueOf, "Input value should be one from the list")
		.bind(Person::getType, Person::setType);
		
	}
	
	public Window callUpdateWindow() {
		updateWindow = new Window("UPDATE USER");
		updateWindow.setVisible(false);
		
		Button updateFormButton = new Button("Update", VaadinIcons.REFRESH);
		updateFormButton.addClickListener(e -> /* checkFields() */ {
			existingEmail(selectedPersonId);
		});
		
		FormLayout content = new FormLayout();
		content.addComponents(fullNameField, usernameField, emailField, passwordField, selectTypeComboBox, updateFormButton);
		content.setSizeUndefined(); 
		content.setMargin(true);
		
		updateWindow.center();
		updateWindow.setContent(content);
		UI.getCurrent().addWindow(updateWindow);
		
		return updateWindow;
	}
	
	MenuBar.Command createNavigationCommand(String navigationView) {
		return new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
		    	navigator.navigateTo(navigationView);
		    }
		};
	}
	
	public void getUserInfo(int userId) {
		try {
			person = personObj.getPersonById(userId);			
			binder.readBean(person);
			updateWindow.setVisible(true);
			
		}
		catch(Exception ex) {
			Notification notif = new Notification("Warning", "Unexpected error!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
	}
	
	public void updatePersonSettings(int userId) {
		try {
			binder.writeBean(person);
			updateInDatabase();
		}
		catch(Exception ex) {
			Notification notif = new Notification("Warning", "Please correct the fields in red!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
	}
	
	public void existingEmail(int userId) {
		try {
			String enteredEmail = emailField.getValue();
	    	person = personObj.getPersonByEmail(enteredEmail.toUpperCase());
	    	if (enteredEmail.equals(person.getEmail())) {
	    		updatePersonSettings(userId);
	    	}
	    	else {
	    	Notification notif = new Notification("Warning", "The email is already used by another user!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
	    	}
	     }
		 catch (Exception ex) {
			 updatePersonSettings(userId);
		 }
	}
	
	public void updateInDatabase() {
		try {
			personObj.updatePersonById(person);
			Notification notif = new Notification("Confirmation!", "Profile successfully updated!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
		catch (Exception ex) {
			Notification notif = new Notification("Warning", "Unexpected error!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
		finally {
			personas = personObj.getAllUsers();
	        grid.setItems(personas);
	        
	        updateWindow.setVisible(false);
	        buttonsHLayout.setVisible(false);
		}
	}


}
