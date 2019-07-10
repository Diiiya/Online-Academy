package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.controller.OrderController;
import com.academy.onlineAcademy.helpView.UserViews;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Order;
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
	
	private static Logger logger = Logger.getLogger(UserCoursesView.class.getName());
	
//	private CourseController courseObj;
	private Grid<com.academy.onlineAcademy.model.Course> grid;
	private int userId;
	
	private OrderController orderObj = new OrderController();
	private CourseController courseObj = new CourseController();
	private List<Course> paidCourses = new ArrayList<Course>();
	
	public UserCoursesView() {
		
//		courseObj = new CourseController();
		initMainLayout();
	
	}
					
	private VerticalLayout initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		
		System.out.println(" User courses ID :" + userId);
		HorizontalLayout layoutH = UserViews.getTopBar(userId);
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
		
		buildGrid();
		layoutV.addComponents(myCoursesLabel, grid);
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
		
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		UI ui = UI.getCurrent();
		VaadinSession session = ui.getSession();
		if (session.getAttribute("user-id") != null) {
			userId = Integer.valueOf(String.valueOf(session.getAttribute("user-id")));
			getAllPaidOrdersOfTheUser(userId);
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
	
}
