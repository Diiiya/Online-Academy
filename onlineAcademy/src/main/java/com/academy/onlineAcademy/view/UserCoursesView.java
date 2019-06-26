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
	CourseController courseObj = new CourseController();
	
	VerticalLayout mainVLayout = new VerticalLayout();
			HorizontalLayout layoutH = new HorizontalLayout();
					String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
					FileResource logoResource = new FileResource(new File(basepath +
				            "/logo.jpg"));
					Image logoImage = new Image("", logoResource);
					MenuBar profileMenu = new MenuBar();
			VerticalLayout layoutV = new VerticalLayout();
					Label myCoursesLabel = new Label("My courses:");
					Grid<com.academy.onlineAcademy.model.Course> grid = new Grid<>();
	
	
	
	public UserCoursesView() {
	
	// 1 - Header bar and UI settings
	layoutH.setSpacing(true);
	layoutH.setWidth("100%");
	layoutH.setHeight("70px");
	
	logoImage.setWidth("130px");
	logoImage.setHeight("60px");
	
	
	// MENU bar and methods to navigate to different pages	
	MenuItem myProfileMainItem = profileMenu.addItem("My profile", VaadinIcons.MENU, null);
	MenuItem myCoursesItem = myProfileMainItem.addItem("My courses", VaadinIcons.ACADEMY_CAP, createNavigationCommand("UserCourses"));
	MenuItem myOrdersItem = myProfileMainItem.addItem("My orders", VaadinIcons.NEWSPAPER, createNavigationCommand("UserOrders"));
	MenuItem mySettingsItem = myProfileMainItem.addItem("Settings", VaadinIcons.USER, createNavigationCommand("Settings"));
	MenuItem myLogoutItem = myProfileMainItem.addItem("Log out", VaadinIcons.EXIT, createNavigationCommand("Home"));

	// Adding the components to layoutH 
	layoutH.addComponents(logoImage, profileMenu);
	layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
	layoutH.setComponentAlignment(profileMenu, Alignment.BOTTOM_RIGHT);
	
	
	// 2 - List with user's courses:
	layoutV.setSpacing(true);
	layoutV.setWidth("100%");
	
	
	// Static - to be replaced with data from the db
	List<Course> courses = Arrays.asList(
			new Course("UX Design", "Some description to be added here", "Sam Johnson", 5, Level.BEGINNER, Category.IT, 50, true, null),
			new Course("Programming basics", "Some description to be added here", "Dean Green", 25, Level.BEGINNER, Category.IT, 130, true, null),
			new Course("Music", "Some description to be added here", "Sara Stevenson", 10, Level.INTERMEDIATE, Category.ARTS, 50, true, null)
			);
	

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
	
	layoutV.addComponents(myCoursesLabel, grid);
	
	mainVLayout.addComponents(layoutH, layoutV);
	mainVLayout.setComponentAlignment(layoutH, Alignment.TOP_CENTER);
	mainVLayout.setComponentAlignment(layoutV, Alignment.MIDDLE_CENTER);
	addComponent(mainVLayout);
	
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
