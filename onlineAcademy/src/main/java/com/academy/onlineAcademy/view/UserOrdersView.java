package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.academy.onlineAcademy.model.Order;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.MenuItem;

public class UserOrdersView extends VerticalLayout implements View {
	
	Navigator navigator = UI.getCurrent().getNavigator();
	
	VerticalLayout mainVLayout = new VerticalLayout();
			HorizontalLayout layoutH = new HorizontalLayout();	
					String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
					FileResource logoResource = new FileResource(new File(basepath +
				            "/logo.jpg"));
					Image logoImage = new Image("", logoResource);
					MenuBar profileMenu = new MenuBar();
			Label myOrdersLabel = new Label("My orders:");
			Grid<com.academy.onlineAcademy.model.Order> grid = new Grid<>();
	
	
	public UserOrdersView() {
		
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
		
		// 2 - Orders
		// Static - to be replaced
		Date date = new Date();
		
		List<Order> orders = Arrays.asList(
				new Order( 1, 1, date, true, 50),
				new Order( 1, 2, date, true, 100),
				new Order( 1, 3, date, true, 300)
				);
		
		grid.setItems(orders);
		grid.setWidth("100%");
		
		grid.addColumn(com.academy.onlineAcademy.model.Order::getUserId).setCaption("User id");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getCourseId).setCaption("Course id");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getPurchaseDate).setCaption("Purchase date");
		grid.addColumn(com.academy.onlineAcademy.model.Order::isPaid).setCaption("Paid ?");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getPrice).setCaption("Price");
		
		mainVLayout.addComponents(layoutH, myOrdersLabel, grid);
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
