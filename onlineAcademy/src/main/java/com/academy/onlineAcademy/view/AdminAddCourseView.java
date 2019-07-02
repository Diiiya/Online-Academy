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
import com.academy.onlineAcademy.helpView.AdminViews;
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
	
	private Navigator navigator;
	Binder<Course> binder;
	
	private final TextField nameField = new TextField("Course name:");
	private final TextField descriptionField = new TextField("Course description:");
	private final TextField teacherNameField = new TextField("Teacher's  name:");
	private final TextField photoField = new TextField("Photo:");	
	private ComboBox<String> selectCategoryComboBox;
	private ComboBox<String> selectLevelComboBox;
	private final TextField durationField = new TextField("Duration:");
	private final TextField priceField = new TextField("Price:");	
	private final CheckBox certCheckbox = new CheckBox("Gives certificate:");
	
	private String name;
	private String description;
	private String teacherName;
	private int duration;
	private Level level;
	private double price;
	private boolean givesCertificate;
//	private File coverPhoto;
//	private FileInputStream fileStream = null;
//	private byte[] convertedCoverPhoto;
					
	public AdminAddCourseView() {
		
		navigator = UI.getCurrent().getNavigator();
		initMainLayout();
        
	}
	
	public void initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		
		HorizontalLayout layoutH = AdminViews.getTopBar(navigator);
		VerticalLayout layoutVBody = getBodyLayout();
		callBinder();
		
		mainVLayout.addComponents(layoutH, layoutVBody);
        addComponent(mainVLayout);
	}
	
	public VerticalLayout getBodyLayout() {
		VerticalLayout layoutVBody = new VerticalLayout();
		layoutVBody.setWidth("100%");
		
		Panel panel = new Panel("Add new course: ");
		panel.setSizeUndefined();
		
		List<String> categories = Stream.of(Category.values()).map(Enum::name).collect(Collectors.toList());
		selectCategoryComboBox = new ComboBox<>("Select category:", categories);
        selectCategoryComboBox.setEmptySelectionAllowed(false);	
        List<String> levels = Stream.of(Level.values()).map(Enum::name).collect(Collectors.toList());
        selectLevelComboBox = new ComboBox<>("Select level:", levels);
		selectLevelComboBox.setValue("BEGINNER");
		selectLevelComboBox.setEmptySelectionAllowed(false);
		certCheckbox.setValue(true);
		
		FormLayout content = new FormLayout();
		content.addComponents(nameField, descriptionField, teacherNameField, photoField, durationField, priceField,
				selectCategoryComboBox, selectLevelComboBox, certCheckbox);
		content.setSizeUndefined(); 
		content.setMargin(true);
		panel.setContent(content);	
		
		Button addButton = new Button("ADD");
		addButton.setWidth("100");
		addButton.addClickListener(e -> {
				
			convertInputValues();
			checkDurationFieldEmpty();

			});
		
		layoutVBody.addComponents(panel, addButton);
		layoutVBody.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		layoutVBody.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
		
		return layoutVBody;
	}
	
	public void callBinder() {
		binder = new Binder<>();
		
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
		
	}
	
	public void convertInputValues() {
		
		name = nameField.getValue();
		description = descriptionField.getValue();
		teacherName = teacherNameField.getValue();
		level = Level.valueOf(selectLevelComboBox.getValue());
		givesCertificate = certCheckbox.getValue();
	
	}
	
	public void checkDurationFieldEmpty() {
		if (durationField.getValue() != "") {
			try {
				duration = Integer.parseInt(durationField.getValue());
				System.out.println("Gets here");
				checkPriceFieldEmpty();
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
	}
	
	public void checkPriceFieldEmpty() {
		if (priceField.getValue() != "") {
			try {
				price = Integer.parseInt(priceField.getValue());
				checkCategoryFieldEmpty();
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
	
	public void checkCategoryFieldEmpty() {
		if (selectCategoryComboBox.getValue() != null) {
			Category category = Category.valueOf(selectCategoryComboBox.getValue());
			NewCourseMethods.addNewCourse(binder, name, description, teacherName, duration, level, category, price, givesCertificate);
		}
		else {
			Notification notif = new Notification("Warning", "The category field should be filled in!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
	}

}
