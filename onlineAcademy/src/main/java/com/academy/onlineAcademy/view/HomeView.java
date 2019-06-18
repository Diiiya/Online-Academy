package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.List;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.model.Course;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class HomeView extends VerticalLayout implements View {
	
	CourseController courseObj = new CourseController();
	Grid<com.academy.onlineAcademy.model.Course> grid = new Grid<>();
	
	public HomeView() {
		
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
		
		// 2 - Cover IMAGE and Search
		FileResource coverResource = new FileResource(new File(basepath +
	            "/1online-courses_0.jpg"));
			
		Image coverImage = new Image("", coverResource);
		coverImage.setWidth("100%");
		coverImage.setHeight("200px");
		
		HorizontalLayout searchHLayout = new HorizontalLayout();
		TextField searchField = new TextField("");
		searchField.setPlaceholder("SEARCH");
		Button searchButton = new Button("Search");
		searchButton.addClickListener(e -> {
			courseObj.getCourseByName(searchField.getValue());
//			List<Course> selectedCourses = courseObj.getAllCourses();
//			grid.setItems(selectedCourses);
			Course selectedCourse = courseObj.getCourseByName(searchField.getValue());
			grid.setItems(selectedCourse);
		});
		searchHLayout.addComponents(searchField, searchButton);
		searchHLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);
		
		// 3 - Top course results:
		Label topCoursesLabel = new Label("Top courses:");
		
//		List<Course> courses = Arrays.asList(
//				new Course("UX Design", "Some description to be added here", "Sam Johnson", 5, Level.BEGINNER, Category.IT, 50, true, null),
//				new Course("Programming basics", "Some description to be added here", "Dean Green", 25, Level.BEGINNER, Category.IT, 130, true, null),
//				new Course("Music", "Some description to be added here", "Sara Stevenson", 10, Level.INTERMEDIATE, Category.ARTS, 50, true, null)
//				);
		
		List<Course> courses = courseObj.getAllCourses();
		
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
		
		mainVLayout.addComponents(layoutH, coverImage, searchHLayout, topCoursesLabel, grid);
		mainVLayout.setComponentAlignment(searchHLayout, Alignment.MIDDLE_CENTER);
		addComponent(mainVLayout);
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
        List<Course> courses = courseObj.getAllCourses();
		
		grid.setItems(courses);
	}

}
