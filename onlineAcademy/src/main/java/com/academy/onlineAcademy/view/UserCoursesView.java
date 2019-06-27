package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.controller.OrderController;
import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.academy.onlineAcademy.model.Order;
import com.vaadin.data.Binder;
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
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class UserCoursesView extends VerticalLayout implements View {
	
	Navigator navigator = UI.getCurrent().getNavigator();
	private CourseController courseObj = new CourseController();
	private Grid<com.academy.onlineAcademy.model.Course> grid = new Grid<>();
					
	public VerticalLayout initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		
		HorizontalLayout layoutH = getTopBar();
		VerticalLayout layoutV = getBodyLayout();
		
		mainVLayout.addComponents(layoutH, layoutV);
		mainVLayout.setComponentAlignment(layoutH, Alignment.TOP_CENTER);
		mainVLayout.setComponentAlignment(layoutV, Alignment.MIDDLE_CENTER);
		addComponent(mainVLayout);
		
		return mainVLayout;
	}
	
	public HorizontalLayout getTopBar() {
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
		
		// MENU bar and methods to navigate to different pages	
		MenuBar profileMenu = new MenuBar();	
		MenuItem myProfileMainItem = profileMenu.addItem("My profile", VaadinIcons.MENU, null);
		MenuItem myCoursesItem = myProfileMainItem.addItem("My courses", VaadinIcons.ACADEMY_CAP, createNavigationCommand("UserCourses"));
		MenuItem myOrdersItem = myProfileMainItem.addItem("My orders", VaadinIcons.NEWSPAPER, createNavigationCommand("UserOrders"));
		MenuItem mySettingsItem = myProfileMainItem.addItem("Settings", VaadinIcons.USER, createNavigationCommand("Settings"));
		MenuItem myLogoutItem = myProfileMainItem.addItem("Log out", VaadinIcons.EXIT, createNavigationCommand("Home"));
		
		layoutH.addComponents(logoImage, profileMenu);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(profileMenu, Alignment.BOTTOM_RIGHT);
		
		return layoutH;
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
	
	public UserCoursesView() {
		
		initMainLayout();
	
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
	
	MenuBar.Command createNavigationCommand(String navigationView) {
		return new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
		    	navigator.navigateTo(navigationView);
		    }
		};
	}
}
