package com.academy.onlineAcademy.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.academy.onlineAcademy.converters.StringToCategoryConverter;
import com.academy.onlineAcademy.converters.StringToLevelConverter;
import com.academy.onlineAcademy.exceptions.NewCourseException;
import com.academy.onlineAcademy.exceptions.NewCourseException.NewCourseTypeError;
import com.academy.onlineAcademy.helpView.AdminViews;
import com.academy.onlineAcademy.helper.ImageUploader;
import com.academy.onlineAcademy.helper.NewCourseMethods;
import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.vaadin.data.Binder;
import com.vaadin.data.Validator;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class AdminAddCourseView extends VerticalLayout implements View {
	private static Logger logger = Logger.getLogger(AdminAddCourseView.class.getName());
	
	private Navigator navigator;
	private Binder<Course> binder;
	
	private final Image image = new Image("Uploaded Image");
	private ImageUploader receiver;
	
	private final TextField nameField = new TextField("Course name:");
	private final TextField descriptionField = new TextField("Course description:");
	private final TextField teacherNameField = new TextField("Teacher's  name:");
	private ComboBox<String> selectCategoryComboBox;
	private ComboBox<String> selectLevelComboBox;
	private final TextField durationField = new TextField("Duration:");
	private final TextField priceField = new TextField("Price:");	
	private final CheckBox certCheckbox = new CheckBox("Gives certificate:");
	private File courseCoverPhotoFile = new File("");
	
	private String name;
	private String description;
	private String teacherName;
	private int duration;
	private Category category;
	private Level level;
	private double price;
	private boolean givesCertificate;
	
	/**
	 * Class constructor
	 */
	public AdminAddCourseView() {
		
		navigator = UI.getCurrent().getNavigator();
		initMainLayout();
        
	}
	
	/**
	 * Builds the main layout
	 */
	public void initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		
		AdminViews adminViews = new AdminViews();
		HorizontalLayout layoutH = adminViews.getTopBar(navigator);
		VerticalLayout layoutVBody = getBodyLayout();
		Panel body = new Panel();
		body.setContent(layoutVBody);
		body.setSizeFull();
		callBinder();
		
		mainVLayout.addComponents(layoutH, body);
        addComponent(mainVLayout);
	}
	
	/**
	 * Layout with the fields to add a new course
	 * @return Vertical layout
	 */
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
		image.setWidth("300px");
		image.setHeight("180px");
		
		receiver = new ImageUploader(image);
		Upload uploadCoursePhoto = new Upload("Course cover image", receiver);
		uploadCoursePhoto.addSucceededListener(receiver);
		content.addComponents(nameField, descriptionField, teacherNameField, uploadCoursePhoto, image, durationField, priceField,
				selectCategoryComboBox, selectLevelComboBox, certCheckbox);
		content.setSizeUndefined(); 
		content.setMargin(true);
		panel.setContent(content);	
		
		Button addButton = new Button("ADD");
		addButton.setWidth("100");
		addButton.addClickListener(e -> addCourse());
		
		layoutVBody.addComponents(panel, addButton);
		layoutVBody.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		layoutVBody.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
		
		return layoutVBody;
	}
	
	/**
	 * Creates binder for the course fields with validators and convertors
	 */
	public void callBinder() {
		binder = new Binder<>();
		
		binder.forField(nameField)
		.asRequired("Cannot be empty")
		.withValidator(new StringLengthValidator("Name must be between 3 and 30 characters long!",3, 30))
	    .bind(Course::getName, Course::setName);
		
		binder.forField(descriptionField)
		.asRequired("Cannot be empty")
		.withValidator(description -> description.length() <= 200, "Description max 200 characters long!")
	    .bind(Course::getDescription, Course::setDescription);
		
		binder.forField(teacherNameField)
		.asRequired("Cannot be empty")
		.withValidator(new StringLengthValidator("Teacher's name must be between 3 and 30 characters long!",3, 30))
	    .bind(Course::getTeacherName, Course::setTeacherName);
		
		binder.forField(durationField)
		.asRequired("Cannot be empty")
		.withConverter(new StringToIntegerConverter("Must enter a number!"))
		.bind(Course::getDuration, Course::setDuration);
		
		binder.forField(priceField)
		.asRequired("Cannot be empty")
		.withConverter(new StringToDoubleConverter("Must enter a decimal number!"))
		.bind(Course::getPrice, Course::setPrice);
		
		binder.forField(selectCategoryComboBox)
		.asRequired("Cannot be empty")
		.withConverter(new StringToCategoryConverter())
		.bind(Course::getCategory, Course::setCategory);
		
		binder.forField(selectLevelComboBox)
		.asRequired("Cannot be empty")
		.withConverter(new StringToLevelConverter())
		.bind(Course::getLevel, Course::setLevel);
		
		binder.forField(certCheckbox)
		.asRequired("Cannot be empty")
		.bind(Course::getGivesCertificate, Course::setGivesCertificate);
		
	}
	
	/**
	 * Adds a new course from the UI; calls the similar method in the controller
	 */
	private void addCourse() {
		try {
			checkValidation();
			convertInputValues();
			NewCourseMethods newCourseMethods = new NewCourseMethods();
			newCourseMethods.addNewCourse(binder, name, description, teacherName, courseCoverPhotoFile, duration, level, category, price, givesCertificate);
		}
		catch (NewCourseException ex) {
			if (ex.getNewCourseTypeError() == NewCourseTypeError.VALIDATION_FAILED) {
				Notification notif = new Notification("Warning", "Correct the field(s) in red.",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(java.util.logging.Level.SEVERE, "Not all fields are filled in as expected! Validation failed!");
			}
			else if (ex.getNewCourseTypeError() == NewCourseTypeError.DATABASE_FAIL) {
				Notification notif = new Notification("Warning", "Failed to save it to the database.",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(java.util.logging.Level.SEVERE, "Failed to add the course to the database!");
			}
		}
	}
	
	/**
	 * Checks if the binder is valid
	 * @return boolean - valid or not valid
	 * @throws NewCourseException
	 */
	private boolean checkValidation() throws NewCourseException {
		if (binder.validate().isOk()) {
			return true;
		}
		else { 
			throw new NewCourseException(NewCourseTypeError.VALIDATION_FAILED);
		}
	}
	
	/**
	 * Converts the input values to values that will be persisted
	 */
	private void convertInputValues() {
		
		name = nameField.getValue();
		description = descriptionField.getValue();
		teacherName = teacherNameField.getValue();
		level = Level.valueOf(selectLevelComboBox.getValue());
		givesCertificate = certCheckbox.getValue();
		duration = Integer.parseInt(durationField.getValue());
		price = Integer.parseInt(priceField.getValue());
		category = Category.valueOf(selectCategoryComboBox.getValue());
		courseCoverPhotoFile = receiver.getFile();
		
//		try {
//			duration = checkDurationFieldEmpty();
//			price = checkPriceFieldEmpty();
//			category = checkCategoryFieldEmpty();
//		}
//		catch (NewCourseException ex) {
//			if (ex.getNewCourseTypeError() == NewCourseException.NewCourseTypeError.DURATION_NUMERIC_VALUE) {
//				Notification notif = new Notification("Warning", "The duration should be a numeric value!", Notification.TYPE_WARNING_MESSAGE);
//				notif.show(Page.getCurrent());
//				
//				logger.log(java.util.logging.Level.SEVERE, "The duration value should be a number!", ex);
//			}
//			else if (ex.getNewCourseTypeError() == NewCourseTypeError.PRICE_NUMERIC_VALUE) {
//				Notification notif = new Notification("Warning", "The price should be a numeric value!", Notification.TYPE_WARNING_MESSAGE);
//				notif.show(Page.getCurrent());
//				
//				logger.log(java.util.logging.Level.SEVERE, "The price value should be a number!", ex);
//			}
//			else if (ex.getNewCourseTypeError() == NewCourseTypeError.CATEGORY_REQUIRED) {
//				Notification notif = new Notification("Warning", "The category field should be filled in!", Notification.TYPE_WARNING_MESSAGE);
//				notif.show(Page.getCurrent());
//				
//				logger.log(java.util.logging.Level.SEVERE, "The category filed cannot be left empty!", ex);
//			}
//		}
	
	}
	
//	private int checkDurationFieldEmpty() throws NewCourseException {
//		if (!durationField.getValue().isBlank()) {
//			parseDuration();
//			return duration;
//		}
//		else {
//			throw new NewCourseException(NewCourseTypeError.DURATION_NUMERIC_VALUE);
//		}
//	}
//	
//	private int parseDuration() throws NewCourseException{
//		try {
//			duration = Integer.parseInt(durationField.getValue());
//			return duration;
//		}
//		catch (Exception ex) {
//			throw new NewCourseException(NewCourseTypeError.DURATION_NUMERIC_VALUE);
//		}
//	}
//	
//	private double checkPriceFieldEmpty() throws NewCourseException {
//		if (!priceField.getValue().isBlank()) {
//			parsePrice();
//			return price;
//		}
//		else {
//			throw new NewCourseException(NewCourseTypeError.PRICE_NUMERIC_VALUE);
//		}
//	}
//	
//	private double parsePrice() throws NewCourseException {
//		try {
//			price = Integer.parseInt(priceField.getValue());
//			return price;
//		}
//		catch (Exception ex){
//			throw new NewCourseException(NewCourseTypeError.PRICE_NUMERIC_VALUE);
//		}
//	}
//	
//	private Category checkCategoryFieldEmpty() throws NewCourseException {
//		try {
//			selectCategoryComboBox.getValue();
//			category = Category.valueOf(selectCategoryComboBox.getValue());
//			return category;
//		}
//		catch (Exception ex) {
//			 throw new NewCourseException(NewCourseTypeError.CATEGORY_REQUIRED);
//		}
//	}

}
