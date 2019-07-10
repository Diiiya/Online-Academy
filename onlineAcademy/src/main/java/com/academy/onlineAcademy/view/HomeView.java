package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.controller.OrderController;
import com.academy.onlineAcademy.helpView.UserViews;
import com.academy.onlineAcademy.helper.NewOrderMethods;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Order;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
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
	
	private static Logger logger = Logger.getLogger(HomeView.class.getName());
	private String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	
	private Navigator navigator;
	private Grid<com.academy.onlineAcademy.model.Course> grid;
	private CourseController courseObj;
	private Button buyCourseButton;
	//private int userId;
	
	private int selectedCourseId;
	private Course selectedCourse;
			
	public HomeView() {
		
		navigator = UI.getCurrent().getNavigator();
		courseObj = new CourseController();
		//initMainlayout();
	}
	
	private void initMainlayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		mainVLayout.setHeight("100%");
		
		HorizontalLayout layoutH = getRightTopBar();
		
		FileResource coverResource = new FileResource(new File(basepath + "/1online-courses_0.jpg"));
		Image coverImage = new Image("", coverResource);
		coverImage.setWidth("100%");
		coverImage.setHeight("200px");
		
		HorizontalLayout searchHLayout = getSearchLayout();
		Label topCoursesLabel = new Label("Top courses:");	
		buildGrid();
		buyCourseButton = new Button("ADD");
		buyCourseButton.setVisible(false);
		buyCourseButton.addClickListener(e -> {
			int userId = getUserId();
			NewOrderMethods.placeOrder(userId, selectedCourse);
		});
		
		mainVLayout.addComponents(layoutH, coverImage, searchHLayout, topCoursesLabel, grid, buyCourseButton);
		mainVLayout.setComponentAlignment(searchHLayout, Alignment.MIDDLE_CENTER);
		addComponent(mainVLayout);
		
	}
			
	private HorizontalLayout getTopBar() {
		HorizontalLayout layoutH = new HorizontalLayout();
		layoutH.setSpacing(true);
		layoutH.setWidth("100%");
		layoutH.setHeight("70px");
		
		Button loginButton = new Button("LOGIN", VaadinIcons.SIGN_IN);
		loginButton.addClickListener(event -> navigator.navigateTo("Login"));
		
		FileResource logoResource = new FileResource(new File(basepath + "/logo.jpg"));
		Image logoImage = new Image("", logoResource);
		logoImage.setWidth("130px");
		logoImage.setHeight("60px");
		
		layoutH.addComponents(logoImage, loginButton);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(loginButton, Alignment.BOTTOM_RIGHT);
		
		return layoutH;
	}
	
	private HorizontalLayout getSearchLayout() {
		HorizontalLayout searchHLayout = new HorizontalLayout();
		
		TextField searchField = new TextField("");
		searchField.setPlaceholder("SEARCH");
		Button searchButton = new Button("Search", VaadinIcons.SEARCH);
		
		searchButton.addClickListener(e -> {
			try {
//				List<Course> selectedCourses = courseObj.getAllCourses();
//				grid.setItems(selectedCourses);
				Course selectedCourse = courseObj.getCourseByName(searchField.getValue().toUpperCase());
				grid.setItems(selectedCourse);
			}
			catch(Exception ex) {
				Notification notif = new Notification("Warning","No course with such name has been found!",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.show(Page.getCurrent());
				
				logger.log(Level.SEVERE, "No course with this name has been found!", ex);
			}

		});
		
		searchHLayout.addComponents(searchField, searchButton);
		searchHLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);
		
		return searchHLayout;
	}
	
	private void buildGrid() {
		
		grid = new Grid<>();
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
			buyCourseButton.setVisible(true);
			selectedCourse = e.getItem();
			selectedCourseId = selectedCourse.getId();
			System.out.println(selectedCourseId);
		});
	}
	
	private HorizontalLayout getRightTopBar() {
		HorizontalLayout layoutH = new HorizontalLayout();
		int userId = getUserId();
		if (userId != 0) {
			System.out.println(" Home page ID :" + userId);
			layoutH = UserViews.getTopBar(userId);
		}
		else {
			System.out.println("Doesn't go inside the else?!");
			layoutH = getTopBar();
		}
		return layoutH;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		initMainlayout();
		
        List<Course> courses = courseObj.getAllCourses();
        grid.setItems(courses);
	}
	
	private int getUserId() {
		int userId;
		UI ui = UI.getCurrent();
		VaadinSession session = ui.getSession();
		if (session.getAttribute("user-id") != null) {
			userId = Integer.valueOf(String.valueOf(session.getAttribute("user-id")));
			System.out.println("USER ID :" + userId);
		}
		else {
			userId = 0;
			System.out.println("USER ID = 0");
		}
		return userId;
	}
	
	

}
