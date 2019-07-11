package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.controller.OrderController;
import com.academy.onlineAcademy.exceptions.LoginException;
import com.academy.onlineAcademy.exceptions.LoginException.LoginErrorType;
import com.academy.onlineAcademy.helpView.UserViews;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Order;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class UserCoursesView extends VerticalLayout implements View {
	
	private static Logger logger = Logger.getLogger(UserCoursesView.class.getName());
	
//	private CourseController courseObj;
	private Navigator navigator;
	private Button viewCourse;
	private Grid<com.academy.onlineAcademy.model.Course> grid;
	private int userId;
	private Course selectedCourse;
	private int courseId;
	
	private OrderController orderObj = new OrderController();
	private CourseController courseObj = new CourseController();
	private List<Course> paidCourses = new ArrayList<Course>();
	private UserViews userViews;
	
	public UserCoursesView() {
		
		userViews = new UserViews();
		navigator = UI.getCurrent().getNavigator();
		selectedCourse = new Course();
//		courseObj = new CourseController();
		initMainLayout();
		refreshLayoutData();
	
	}
					
	private VerticalLayout initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		
		System.out.println(" User courses ID :" + userId);
		HorizontalLayout layoutH = userViews.getTopBar(userId);
		VerticalLayout layoutV = getBodyLayout();
		
		mainVLayout.addComponents(layoutH, layoutV);
		mainVLayout.setComponentAlignment(layoutH, Alignment.TOP_CENTER);
		mainVLayout.setComponentAlignment(layoutV, Alignment.MIDDLE_CENTER);
		addComponent(mainVLayout);
		
		return mainVLayout;
	}
	
	private VerticalLayout getBodyLayout() {
		VerticalLayout layoutV = new VerticalLayout();
		Label myCoursesLabel = new Label("My courses:");
		layoutV.setSpacing(true);
		layoutV.setWidth("100%");
		viewCourse = new Button("View Course");
		buildGrid();
		viewCourse.setVisible(false);
		viewCourse.addClickListener(e -> getSetCourse(selectedCourse));
		layoutV.addComponents(myCoursesLabel, grid, viewCourse);
		return layoutV;
	}
	
	private void buildGrid() {
		
		grid = new Grid<>();
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
		
		grid.addItemClickListener(e -> {
			selectedCourse = e.getItem();
			viewCourse.setVisible(true);
		});
	}

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);	
		refreshLayoutData();
	}
	
	private void refreshLayoutData() {
		UI ui = UI.getCurrent();
		VaadinSession session = ui.getSession();
		if (session.getAttribute("user-id") != null) {
			userId = Integer.valueOf(String.valueOf(session.getAttribute("user-id")));
			getAllPaidOrdersOfTheUser(userId);
			userViews.setLabelValue(userId);
			System.out.println("USER Courses userId:" + userId);
			if (session.getAttribute("course-id") != null) {
				courseId = Integer.valueOf(String.valueOf(session.getAttribute("course-id")));
				System.out.println("Course id:" + courseId);
			}
			else {
				System.out.println("USER ID VAL:" + session.getAttribute("user-id"));
			}
		}
		else {
			System.out.println("USER ID VAL:" + session.getAttribute("user-id"));
		}
	}
	
	private void getAllPaidOrdersOfTheUser(int userId) {
		try {
			List<Order> paidOrders = orderObj.getAllPaidOrdersByUser(userId);
			for (Order order : paidOrders) {
				int courseId = order.getCourseId();
				Course course = courseObj.getCourseById(courseId);
				if(!paidCourses.contains(course)) {
					paidCourses.add(course);
				}
				
			}
			grid.setItems(paidCourses);
		}
		catch (Exception ex) {
			Notification notif = new Notification("Warning!", "No paid orders for this user have been found!");
			notif.show(Page.getCurrent());
			
			logger.log(Level.SEVERE, "No paid orders for this user have been found!", ex);
		}
	}

	
	private void getSetCourse(Course selectedCourse) {
		UI ui = UI.getCurrent();
		VaadinSession session = ui.getSession();
			
		session.setAttribute("course-id", selectedCourse.getId());
		System.out.println("Course id from UserCourses ~ " + selectedCourse.getId());
		navigator.navigateTo("Course" + "/" + selectedCourse.getName().replaceAll("\\s", "-"));
		
	}
	
}
