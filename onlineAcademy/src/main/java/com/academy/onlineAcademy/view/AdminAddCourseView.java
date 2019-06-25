package com.academy.onlineAcademy.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.helper.NewCourseMethods;
import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.MenuItem;

public class AdminAddCourseView extends VerticalLayout implements View {
	
	String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	FileResource logoResource = new FileResource(new File(basepath + "/logo.jpg"));
	FileResource coverResource = new FileResource(new File(basepath + "/1online-courses_0.jpg"));
	
	Navigator navigator = UI.getCurrent().getNavigator();
	Binder<Course> binder = new Binder<>();
	CourseController obj = new CourseController();
	// Fields
	String name;
	String description;
	String teacherName;
	int duration;
	Level level;
	Category category;
	double price;
	boolean givesCertificate;
	File coverPhoto;
	FileInputStream fileStream = null;
	byte[] convertedCoverPhoto;
	
	
	VerticalLayout mainVLayout = new VerticalLayout();
			HorizontalLayout layoutH = new HorizontalLayout();
					Image logoImage = new Image("", logoResource);
					MenuBar profileMenu = new MenuBar();
			VerticalLayout layoutVBody = new VerticalLayout();
					Panel panel = new Panel("Add new course: ");
							FormLayout content = new FormLayout();
									TextField nameField = new TextField("Course name:");
									TextField descriptionField = new TextField("Course description:");
									TextField teacherNameField = new TextField("Teacher's  name:");
									TextField photoField = new TextField("Photo:");	
									List<String> categories = Stream.of(Category.values())
								            .map(Enum::name)
								            .collect(Collectors.toList());
									ComboBox<String> selectCategoryComboBox = new ComboBox<>("Select category:", categories);
									List<String> levels = Stream.of(Level.values())
								            .map(Enum::name)
								            .collect(Collectors.toList());
									ComboBox<String> selectLevelComboBox = new ComboBox<>("Select level:", levels);
									TextField durationField = new TextField("Duration:");
									TextField priceField = new TextField("Price:");	
									CheckBox certCheckbox = new CheckBox("Gives certificate:");
					Button addButton = new Button("ADD");
	
	public AdminAddCourseView() {
		
		// 1 - Header bar & UI settings
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
		
		// 2 - Add course panel
		layoutVBody.setWidth("100%");
		panel.setSizeUndefined();
		
		selectCategoryComboBox.setEmptySelectionAllowed(false);
		
		selectLevelComboBox.setValue("BEGINNER");
		selectLevelComboBox.setEmptySelectionAllowed(false);
		
		certCheckbox.setValue(true);
		
		content.addComponents(nameField, descriptionField, teacherNameField, photoField, durationField, priceField,
				selectCategoryComboBox, selectLevelComboBox, certCheckbox);
		content.setSizeUndefined(); 
		content.setMargin(true);
		panel.setContent(content);		
		
		///////// BINDER Part + validations
		binder.forField(nameField).withValidator(new StringLengthValidator("Name must be between 3 and 30 characters long!",3, 30)).asRequired("Cannot be empty")
	    .bind(Course::getName, Course::setName);
		binder.forField(descriptionField).withValidator(description -> description.length() <= 200, "Description max 200 characters long!").asRequired("Cannot be empty")
	    .bind(Course::getDescription, Course::setDescription);
		binder.forField(teacherNameField).withValidator(new StringLengthValidator("Teacher's name must be between 3 and 30 characters long!",3, 30)).asRequired("Cannot be empty")
	    .bind(Course::getTeacherName, Course::setTeacherName);
		binder.forField(durationField).withConverter(new StringToIntegerConverter("Must enter a number!")).asRequired("Cannot be empty")
		.bind(Course::getDuration, Course::setDuration);
		binder.forField(priceField).withConverter(new StringToDoubleConverter("Must enter a decimal number!")).asRequired("Cannot be empty")
		.bind(Course::getPrice, Course::setPrice);
		binder.forField(selectCategoryComboBox).asRequired("Cannot be empty");
		
		
		addButton.setWidth("100");
		addButton.addClickListener(e -> {
				
				// Gets and converts the input values
				name = nameField.getValue();
				description = descriptionField.getValue();
				teacherName = teacherNameField.getValue();
				level = Level.valueOf(selectLevelComboBox.getValue());
				givesCertificate = certCheckbox.getValue();
				if (durationField.getValue() != "") {
					try {
					duration = Integer.parseInt(durationField.getValue());
					System.out.println("Gets here");
						
							if (priceField.getValue() != "") {
								try {
								price = Integer.parseInt(priceField.getValue());
									if (selectCategoryComboBox.getValue() != null) {
										category = Category.valueOf(selectCategoryComboBox.getValue());
										NewCourseMethods.addNewCourse(binder, name, description, teacherName, duration, level, category, price, givesCertificate);
									}
									else {
										Notification notif = new Notification("Warning", "The category field should be filled in!", Notification.TYPE_WARNING_MESSAGE);
										notif.show(Page.getCurrent());
									}
								}
								catch (Exception ex){
									Notification notif = new Notification("Warning", "The price should be a numeric value!", Notification.TYPE_WARNING_MESSAGE);
									notif.show(Page.getCurrent());
								}
							}
							else {
								Notification notif = new Notification("Warning", "The price should be a filled in!", Notification.TYPE_WARNING_MESSAGE);
								notif.show(Page.getCurrent());
							}
						}
					catch (Exception ex) {
						Notification notif = new Notification("Warning", "The duration should be a numeric value!", Notification.TYPE_WARNING_MESSAGE);
						notif.show(Page.getCurrent());
					}
				}
				else {
					Notification notif = new Notification("Warning", "The duration field should be filled in!", Notification.TYPE_WARNING_MESSAGE);
					notif.show(Page.getCurrent());
				}

			});
		
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
