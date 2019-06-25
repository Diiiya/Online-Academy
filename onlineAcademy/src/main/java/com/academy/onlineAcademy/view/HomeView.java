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
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class HomeView extends VerticalLayout implements View {
	
	String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	FileResource logoResource = new FileResource(new File(basepath + "/logo.jpg"));
	FileResource coverResource = new FileResource(new File(basepath + "/1online-courses_0.jpg"));	
	
	Navigator navigator = UI.getCurrent().getNavigator();
	CourseController courseObj = new CourseController();
	List<Course> courses;
	Course selectedCourse;
	int selectedCourseId;
	
	VerticalLayout mainVLayout = new VerticalLayout();
			HorizontalLayout layoutH = new HorizontalLayout();
					Button loginButton = new Button("LOGIN", VaadinIcons.SIGN_IN);
					Image logoImage = new Image("", logoResource);				
			Image coverImage = new Image("", coverResource);
			HorizontalLayout searchHLayout = new HorizontalLayout();
					TextField searchField = new TextField("");
					Button searchButton = new Button("Search", VaadinIcons.SEARCH);
			Label topCoursesLabel = new Label("Top courses:");	
			Grid<com.academy.onlineAcademy.model.Course> grid = new Grid<>();
	
	public HomeView() {
		
		mainVLayout.setHeight("100%");
		
		// 1 - Header bar & UI settings
		layoutH.setSpacing(true);
		layoutH.setWidth("100%");
		layoutH.setHeight("70px");
		
		logoImage.setWidth("130px");
		logoImage.setHeight("60px");
		
		loginButton.addClickListener(event -> {
			navigator.navigateTo("Login");
		});
		
		
		layoutH.addComponents(logoImage, loginButton);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(loginButton, Alignment.BOTTOM_RIGHT);
		
		// 2 - Cover IMAGE and Search
		coverImage.setWidth("100%");
		coverImage.setHeight("200px");
		
		searchField.setPlaceholder("SEARCH");
		searchButton.addClickListener(e -> {
			try {
				courseObj.getCourseByName(searchField.getValue());
//				List<Course> selectedCourses = courseObj.getAllCourses();
//				grid.setItems(selectedCourses);
				Course selectedCourse = courseObj.getCourseByName(searchField.getValue());
				grid.setItems(selectedCourse);
			}
			catch(Exception ex) {
				Notification notif = new Notification(
					    "Warning",
					    "No course with such name have been found!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
			}

		});
		searchHLayout.addComponents(searchField, searchButton);
		searchHLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);
		
		// 3 - Top course results:		
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
