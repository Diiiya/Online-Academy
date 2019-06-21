package com.academy.onlineAcademy.view;

import java.io.File;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.MenuItem;

public class CourseView extends VerticalLayout implements View {
	
	Navigator navigator = UI.getCurrent().getNavigator();
	
	VerticalLayout mainVLayout = new VerticalLayout();
			HorizontalLayout layoutH = new HorizontalLayout();
					String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
					FileResource logoResource = new FileResource(new File(basepath +
				            "/logo.jpg"));		
					Image logoImage = new Image("", logoResource);
					MenuBar profileMenu = new MenuBar();
			HorizontalLayout layoutBody = new HorizontalLayout();
					VerticalLayout layoutVL = new VerticalLayout();
							Label courseTitleLabel = new Label("COURSE TITLE");
							FileResource courseImageResource = new FileResource(new File(basepath +
						            "/1online-courses_0.jpg"));
							Image courseImage = new Image("", courseImageResource);
							Label courseContentLabel = new Label("Course Content Here ...........");
					VerticalLayout layoutVR = new VerticalLayout();
					        Label similarCoursesLabel = new Label("Similar courses:");
	
					        
					        
	public CourseView() {
		
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
		
		layoutH.addComponents(logoImage, profileMenu);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(profileMenu, Alignment.BOTTOM_RIGHT);
		
		// 2 - Body
		// 2.1 - Course content - Left part
		layoutVL.setWidth("1200px");
		courseImage.setWidth("600px");
		courseImage.setHeight("300px");
		layoutVL.addComponents(courseTitleLabel, courseImage, courseContentLabel);
		
		// 2.2 - Similar courses - Right bar
		layoutVR.setWidth("600px");
		layoutVR.addComponents(similarCoursesLabel);
		
		layoutBody.addComponents(layoutVL, layoutVR);
		
		mainVLayout.addComponents(layoutH, layoutBody);
		addComponent(mainVLayout);
	}

	
	
	MenuBar.Command createNavigationCommand(String navigationView) {
		return new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
		    	navigator.navigateTo(navigationView);
		    }
		};
	}
	
}
