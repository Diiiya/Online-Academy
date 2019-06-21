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
	
	public UserOrdersView() {
		
		VerticalLayout mainVLayout = new VerticalLayout();
		
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
		
		// MENU bar and methods to navigate to different pages
		MenuBar.Command goToUsersCourses = new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
		    	navigator.navigateTo("UserCourses");
		    }
		};
		
		MenuBar.Command goToUserOrders = new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
		    	navigator.navigateTo("UserOrders");
		    }
		};
		
		MenuBar.Command goToUserSettings = new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
		    	navigator.navigateTo("Settings");
		    }
		};
		
		MenuBar.Command logout = new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
		    	navigator.navigateTo("Home");
		    }
		};
		
		MenuBar profileMenu = new MenuBar();
		MenuItem myProfileMainItem = profileMenu.addItem("My profile", VaadinIcons.MENU, null);
		MenuItem myCoursesItem = myProfileMainItem.addItem("My courses", VaadinIcons.ACADEMY_CAP, goToUsersCourses);
		MenuItem myOrdersItem = myProfileMainItem.addItem("My orders", VaadinIcons.NEWSPAPER, goToUserOrders);
		MenuItem mySettingsItem = myProfileMainItem.addItem("Settings", VaadinIcons.USER, goToUserSettings);
		MenuItem myLogoutItem = myProfileMainItem.addItem("Log out", VaadinIcons.EXIT, logout);
		
		layoutH.addComponents(logoImage, profileMenu);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(profileMenu, Alignment.BOTTOM_RIGHT);
		
		// 2 - Orders
		Label myOrdersLabel = new Label("My orders:");
		
		Date date = new Date();
		
		List<Order> orders = Arrays.asList(
				new Order( 1, 1, date, true, 50),
				new Order( 1, 2, date, true, 100),
				new Order( 1, 3, date, true, 300)
				);
		
		Grid<com.academy.onlineAcademy.model.Order> grid = new Grid<>();
		grid.setItems(orders);
		
		grid.addColumn(com.academy.onlineAcademy.model.Order::getCourseId).setCaption("Course id");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getPurchaseDate).setCaption("Purchase date");
		grid.addColumn(com.academy.onlineAcademy.model.Order::isPaid).setCaption("Course description");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getPrice).setCaption("Price");
		
		grid.setWidth("100%");
		
		mainVLayout.addComponents(layoutH, myOrdersLabel, grid);
		addComponent(mainVLayout);
	}

}
