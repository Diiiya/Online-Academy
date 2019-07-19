package com.academy.onlineAcademy.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.helpView.AdminViews;
import com.academy.onlineAcademy.helper.UpdateCourseMethods;
import com.academy.onlineAcademy.helper.UpdateUserMethods;
import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.sass.ArgumentParser.Option;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
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
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.MenuBar.MenuItem;

public class AdminAllCoursesView extends VerticalLayout implements View {
	
	private static Logger logger = Logger.getLogger(AdminAllCoursesView.class.getName());
	
	private Navigator navigator;
	private Window updateWindow;
	
	private Binder<Course> binder;
	private final TextField nameField = new TextField("Course name:");
	private final TextField descriptionField = new TextField("Course description:");
	private final TextField teacherNameField = new TextField("Teacher's  name:");
	private final TextField photoField = new TextField("Photo:");
	
	private List<String> categories;	
	private ComboBox<String> selectCategoryComboBox;	
	private List<String> levels;	
	private ComboBox<String> selectLevelComboBox;
	
	private HorizontalLayout buttonsHLayout;
	private final TextField durationField = new TextField("Duration:");
	private final TextField priceField = new TextField("Price:");	
	private final CheckBox certCheckbox = new CheckBox("Gives certificate:");
	
	private CourseController courseObj;
	private List<Course> courses;
	private Grid<com.academy.onlineAcademy.model.Course> grid;
	private Course selectedCourse;
	private int selectedCourseId;
	UpdateCourseMethods updateCourseMethods;
	
	/**
	 * Class constructor
	 */
	public AdminAllCoursesView() {
		
		navigator = UI.getCurrent().getNavigator();
		courseObj = new CourseController();
		updateCourseMethods = new UpdateCourseMethods();
		initMainLayout();		
		
	}
	
	/**
	 * Initializes the main layout
	 * @return vertical layout
	 */
	private VerticalLayout initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		mainVLayout.setHeight("100%");
		
		AdminViews adminViews = new AdminViews();
		HorizontalLayout layoutH = adminViews.getTopBar(navigator);
		HorizontalLayout searchHLayout = getSearchLayout();
		certCheckbox.setValue(true);
		Label allCoursesLabel = new Label("All courses:");	
		buildGrid();
		buttonsHLayout = getButtonsDELIUPDLayout();
		callUpdateWindow();
		binder = getBinder();
		
		mainVLayout.addComponents(layoutH, searchHLayout, allCoursesLabel, grid, buttonsHLayout);
		mainVLayout.setComponentAlignment(searchHLayout, Alignment.MIDDLE_CENTER);
		mainVLayout.setComponentAlignment(buttonsHLayout, Alignment.BOTTOM_CENTER);
		addComponent(mainVLayout);
		
		return mainVLayout;
	}
	
	/**
	 * Creates search field and button for courses
	 * @return HorizontalLayout
	 */
	private HorizontalLayout getSearchLayout() {
		HorizontalLayout searchHLayout = new HorizontalLayout();
		TextField searchField = new TextField("");
		searchField.setPlaceholder("SEARCH");
		Button searchButton = new Button("Search", VaadinIcons.SEARCH);
		searchButton.addClickListener(e -> {
			try { 
//				List<Course> selectedCourses = courseObj.getAllCourses();
//				grid.setItems(selectedCourses);
				String searchedCourse = searchField.getValue().toUpperCase();
				Course selectedCourse = courseObj.getCourseByName(searchedCourse);
				grid.setItems(selectedCourse);
			}
			catch (Exception ex) {
				Notification notif = new Notification("Warning!", "No course with this name has been found!", Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(java.util.logging.Level.SEVERE, "No course with this name has been found!", ex);
			}
			
		});
		searchHLayout.addComponents(searchField, searchButton);
		searchHLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);
		
		return searchHLayout;
	}
	
	/**
	 * Creates grid that displays Courses data in table
	 */
	private void buildGrid() {
		grid = new Grid<Course>();
		
		courses = courseObj.getAllCourses();
		grid.setItems(courses);		
		
		grid.addColumn(com.academy.onlineAcademy.model.Course::getId).setCaption("Id");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getName).setCaption("Course name");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getDescription).setCaption("Course description");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getTeacherName).setCaption("Teacher");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getCategory).setCaption("Category");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getDuration).setCaption("Duration");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getLevel).setCaption("Level");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getPrice).setCaption("Price in euros");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getGivesCertificate).setCaption("Gives certificate");
		
		grid.setWidth("100%");
		
		grid.addItemClickListener(e -> {
			selectedCourse = e.getItem();
			selectedCourseId = selectedCourse.getId();
			buttonsHLayout.setVisible(true);
		});
	}
	
	/**
	 * Creates a update and delete course buttons with events
	 * @return
	 */
	private HorizontalLayout getButtonsDELIUPDLayout() {
		buttonsHLayout = new HorizontalLayout();
		
		buttonsHLayout.setVisible(false);
		Button deleteCourseButton = new Button("Delete", VaadinIcons.DEL);
		deleteCourseButton.setWidth("200px");
		Button updateCourseButton = new Button("Update", VaadinIcons.REFRESH);
		updateCourseButton.setWidth("200px");
		buttonsHLayout.addComponents(updateCourseButton, deleteCourseButton);
		
		deleteCourseButton.addClickListener(e -> {
			try {
				courseObj.deleteCourseById(selectedCourseId);
		        courses = courseObj.getAllCourses();
		        grid.setItems(courses);
		        buttonsHLayout.setVisible(false);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		});
		
		updateCourseButton.addClickListener(e -> {
			updateCourseMethods.getCourseInfo(selectedCourseId, binder, courseObj, selectedCourse);
			updateWindow.setVisible(true);
		});
		
		return buttonsHLayout;
	}
	
	/**
	 * Creates binder with validators and converters for the Course class
	 * @return Binder<Course>
	 */
	private Binder<Course> getBinder() {
		binder = new Binder<>();
		
		binder.forField(nameField).withValidator(new StringLengthValidator("Name must be between 3 and 30 characters long!",3, 30)).asRequired("Cannot be empty")
	    .bind(Course::getName, Course::setName);
		binder.forField(descriptionField).withValidator(description -> description.length() <= 200, "Description max 200 characters long!").asRequired("Cannot be empty")
	    .bind(Course::getDescription, Course::setDescription);
		binder.forField(teacherNameField).withValidator(new StringLengthValidator("Teacher's name must be between 3 and 30 characters long!",3, 30)).asRequired("Cannot be empty")
	    .bind(Course::getTeacherName, Course::setTeacherName);
		
//		.withConverter(Integer::valueOf, String::valueOf, "Input value should be an integer")
		binder.forField(selectCategoryComboBox)
		.withConverter(Category::valueOf, String::valueOf, "Input value should be one from the list")
		.asRequired("Cannot be empty")
		.bind(Course::getCategory, Course::setCategory);
		binder.forField(durationField).withConverter(new StringToIntegerConverter("Must enter a number!")).asRequired("Cannot be empty")
		.bind(Course::getDuration, Course::setDuration);
		binder.forField(selectLevelComboBox).asRequired("Cannot be empty")
		.withConverter(Level::valueOf, String::valueOf, "Input value should be one from the list")
		.bind(Course::getLevel, Course::setLevel);
		
		binder.forField(priceField).withConverter(new StringToDoubleConverter("Must enter a decimal number!")).asRequired("Cannot be empty")
		.bind(Course::getPrice, Course::setPrice);
		binder.forField(certCheckbox).bind(Course::getGivesCertificate, Course::setGivesCertificate);
		
		return binder;
	}
	
	/**
	 * Creates a window with fields to update a course
	 * @return Window
	 */
	private Window callUpdateWindow() { 
		updateWindow = new Window("UPDATE COURSE");
		updateWindow.setVisible(false);
	
		categories = Stream.of(Category.values()).map(Enum::name).collect(Collectors.toList());
		selectCategoryComboBox = new ComboBox<>("Select category:", categories);
		selectCategoryComboBox.setEmptySelectionAllowed(false);
		levels = Stream.of(Level.values()).map(Enum::name).collect(Collectors.toList());
		selectLevelComboBox = new ComboBox<>("Select level:", levels);
		selectLevelComboBox.setEmptySelectionAllowed(false);
		
		Button updateFormButton = new Button("Update", VaadinIcons.REFRESH);
		updateFormButton.addClickListener(e -> {
			String courseName = nameField.getValue();
			boolean isSuccessful = updateCourseMethods.updateCourse(selectedCourseId, binder, courseObj, selectedCourse, courseName);	
			if (isSuccessful == true) {
				courses = courseObj.getAllCourses();
		        grid.setItems(courses);
		        
		        updateWindow.setVisible(false);
		        buttonsHLayout.setVisible(false);
			}
		});
		
		FormLayout content = new FormLayout();
		content.addComponents(nameField, descriptionField, teacherNameField, photoField, durationField, priceField,
				selectCategoryComboBox, selectLevelComboBox, certCheckbox, updateFormButton);
		content.setSizeUndefined(); 
		content.setMargin(true);
		
		updateWindow.center();
		updateWindow.setContent(content);
		
		UI.getCurrent().addWindow(updateWindow);
		
		return updateWindow;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);		
		courses = courseObj.getAllCourses();		
		grid.setItems(courses);
	}


}
