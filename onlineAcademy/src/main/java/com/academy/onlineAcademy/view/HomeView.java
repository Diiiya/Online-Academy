package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.List;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.model.Course;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class HomeView extends VerticalLayout implements View {
	
	CourseController courseObj = new CourseController();
	Grid<com.academy.onlineAcademy.model.Course> grid = new Grid<>();
	Course selectedCourse;
	int selectedCourseId;
	
	public HomeView() {
		
		VerticalLayout mainVLayout = new VerticalLayout();
		mainVLayout.setHeight("100%");
		Navigator navigator = UI.getCurrent().getNavigator();
		
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
		loginButton.addClickListener(event -> {
			navigator.navigateTo("Login");
		});
		
		
		layoutH.addComponents(logoImage, loginButton);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(loginButton, Alignment.BOTTOM_RIGHT);
		
		// 2 - Cover IMAGE and Search
		FileResource coverResource = new FileResource(new File(basepath +
	            "/1online-courses_0.jpg"));
			
		Image coverImage = new Image("", coverResource);
		coverImage.setWidth("100%");
		coverImage.setHeight("200px");
		
		HorizontalLayout searchHLayout = new HorizontalLayout();
		TextField searchField = new TextField("");
		searchField.setPlaceholder("SEARCH");
		Button searchButton = new Button("Search", VaadinIcons.SEARCH);
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
		
		grid.addItemClickListener(e -> {
			Course selectedCourse = e.getItem();
			selectedCourseId = selectedCourse.getId();
			System.out.println(selectedCourseId);
		});

		
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
