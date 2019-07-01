package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.helpView.UserViews;
import com.academy.onlineAcademy.model.Course;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class UserCoursesView extends VerticalLayout implements View {
	
	Navigator navigator = UI.getCurrent().getNavigator();
	private CourseController courseObj = new CourseController();
	private Grid<com.academy.onlineAcademy.model.Course> grid = new Grid<>();
	
	public UserCoursesView() {
		
		initMainLayout();
	
	}
					
	public VerticalLayout initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		
		HorizontalLayout layoutH = UserViews.getTopBar(navigator);
		VerticalLayout layoutV = getBodyLayout();
		
		mainVLayout.addComponents(layoutH, layoutV);
		mainVLayout.setComponentAlignment(layoutH, Alignment.TOP_CENTER);
		mainVLayout.setComponentAlignment(layoutV, Alignment.MIDDLE_CENTER);
		addComponent(mainVLayout);
		
		return mainVLayout;
	}
	
	public VerticalLayout getBodyLayout() {
		VerticalLayout layoutV = new VerticalLayout();
		Label myCoursesLabel = new Label("My courses:");
		layoutV.setSpacing(true);
		layoutV.setWidth("100%");
		
		grid = getGrid();
		layoutV.addComponents(myCoursesLabel, grid);
		return layoutV;
	}
	
	public Grid<Course> getGrid() {
		grid.setWidth("100%");
		
		grid.addColumn(com.academy.onlineAcademy.model.Course::getId).setCaption("Id");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getName).setCaption("Course name");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getDescription).setCaption("Course description");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getTeacherName).setCaption("Teacher");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getCategory).setCaption("Category");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getDuration).setCaption("Duration");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getLevel).setCaption("Level");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getPrice).setCaption("Price in euros");
		grid.addColumn(com.academy.onlineAcademy.model.Course::getGivesCertificate).setCaption("Gives certificate");
		
		return grid;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		UI ui = UI.getCurrent();
		VaadinSession session = ui.getSession();
		if (session.getAttribute("user-id") != null) {
			int userId = Integer.valueOf(String.valueOf(session.getAttribute("user-id")));
			getAllTheCoursesOfTheUser(userId);
		}
		else {
			System.out.println("USER ID VAL:" + session.getAttribute("user-id"));
		}
	}
	
	public void getAllTheCoursesOfTheUser(int userId) {
		try {
			//List<Course> courses = courseObj.getAllCoursesByUser(userId);
			//grid.setItems(courses);
		}
		catch(Exception ex) {
			Notification notif = new Notification(
				    "Warning",
				    "No course(s) for this user have been found!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
	}
	
}
