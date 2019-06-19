package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
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
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AdminAllCoursesView extends VerticalLayout implements View {
	
	CourseController courseObj = new CourseController();
	List<Course> courses;
	Grid<com.academy.onlineAcademy.model.Course> grid = new Grid<>();
	Course selectedCourse;
	int selectedCourseId;
	
	public AdminAllCoursesView() {
		
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
		Button loginButton = new Button("LOGIN", VaadinIcons.SIGN_IN);
		
		
		layoutH.addComponents(logoImage, loginButton);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(loginButton, Alignment.BOTTOM_RIGHT);
		
		// 2 - Search
				
				HorizontalLayout searchHLayout = new HorizontalLayout();
				TextField searchField = new TextField("");
				searchField.setPlaceholder("SEARCH");
				Button searchButton = new Button("Search", VaadinIcons.SEARCH);
				searchButton.addClickListener(e -> {
					courseObj.getCourseByName(searchField.getValue());
//					List<Course> selectedCourses = courseObj.getAllCourses();
//					grid.setItems(selectedCourses);
					Course selectedCourse = courseObj.getCourseByName(searchField.getValue());
					grid.setItems(selectedCourse);
				});
				searchHLayout.addComponents(searchField, searchButton);
				searchHLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);
				
				// 3 - Top course results:
				Label topCoursesLabel = new Label("Top courses:");		
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
				grid.setHeight("100%");
				
				HorizontalLayout buttonsHLayout = new HorizontalLayout();
				buttonsHLayout.setVisible(false);
				Button deleteCourseButton = new Button("Delete", VaadinIcons.DEL);
				deleteCourseButton.setWidth("200px");
				Button updateCourseButton = new Button("Update", VaadinIcons.REFRESH);
				updateCourseButton.setWidth("200px");
				buttonsHLayout.addComponents(updateCourseButton, deleteCourseButton);
				
				grid.addItemClickListener(e -> {
					selectedCourse = e.getItem();
					selectedCourseId = selectedCourse.getId();
					System.out.println(selectedCourseId);
					buttonsHLayout.setVisible(true);
				});

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
				
				//
				//// - Update button and popup content
				//
				
				Panel popupContent = new Panel("Add new course: ");
				popupContent.setSizeUndefined();
				
				TextField nameField = new TextField("Course name:");
				TextField descriptionField = new TextField("Course description:");
				TextField teacherNameField = new TextField("Teacher's  name:");
				TextField photoField = new TextField("Photo:");
				
				List<String> categories = Stream.of(Category.values())
		                .map(Enum::name)
		                .collect(Collectors.toList());
				
				ComboBox<String> selectCategoryComboBox = new ComboBox<>("Select category:", categories);
				selectCategoryComboBox.setEmptySelectionAllowed(false);
				
				List<String> levels = Stream.of(Level.values())
		                .map(Enum::name)
		                .collect(Collectors.toList());
				
				ComboBox<String> selectLevelComboBox = new ComboBox<>("Select level:", levels);
				selectLevelComboBox.setValue("BEGINNER");
				selectLevelComboBox.setEmptySelectionAllowed(false);
				

				TextField durationField = new TextField("Duration:");
				TextField priceField = new TextField("Price:");
				
				CheckBox certCheckbox = new CheckBox("Gives certificate:");
				certCheckbox.setValue(true);
				
				Binder<Course> binder = new Binder<>();
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
				
				FormLayout content = new FormLayout();
				content.addComponents(nameField, descriptionField, teacherNameField, photoField, durationField, priceField,
						selectCategoryComboBox, selectLevelComboBox, certCheckbox);
				content.setSizeUndefined(); 
				content.setMargin(true);
				popupContent.setContent(content);
				
				PopupView popup = new PopupView(null, popupContent);
				
				updateCourseButton.addClickListener(e -> {
					popup.setPopupVisible(true);
					if (selectedCourse != null) {
					nameField.setValue(selectedCourse.getName());
					descriptionField.setValue(selectedCourse.getDescription());
					teacherNameField.setValue(selectedCourse.getTeacherName());
					selectCategoryComboBox.setValue(selectedCourse.getCategory().toString());
					selectLevelComboBox.setValue(selectedCourse.getLevel().toString());
					//durationField.setValue(selectedCourse.getDuration());
					System.out.println("NOT NULL");
					}
				});
				
				
				mainVLayout.addComponents(layoutH, searchHLayout, topCoursesLabel, grid, buttonsHLayout, popup);
				mainVLayout.setComponentAlignment(searchHLayout, Alignment.MIDDLE_CENTER);
				mainVLayout.setComponentAlignment(buttonsHLayout, Alignment.BOTTOM_CENTER);
				mainVLayout.setComponentAlignment(popup, Alignment.MIDDLE_CENTER);
				addComponent(mainVLayout);
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
        courses = courseObj.getAllCourses();
		
		grid.setItems(courses);
	}

}
