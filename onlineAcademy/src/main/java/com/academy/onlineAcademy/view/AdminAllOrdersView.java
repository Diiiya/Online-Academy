package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.List;

import com.academy.onlineAcademy.controller.OrderController;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.MenuItem;

public class AdminAllOrdersView extends VerticalLayout implements View {
	
	Navigator navigator = UI.getCurrent().getNavigator();
	OrderController orderObj = new OrderController();
	List<Order> orders;
	Grid<com.academy.onlineAcademy.model.Order> grid = new Grid<>();
	Order selectedOrder;
	int selectedOrderId;
	
	public AdminAllOrdersView() {
		VerticalLayout mainVLayout = new VerticalLayout();
		mainVLayout.setHeight("100%");
		
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
		MenuBar profileMenu = new MenuBar();
		MenuItem myProfileMainItem = profileMenu.addItem("My profile", VaadinIcons.MENU, null);
		MenuItem allCoursesItem = myProfileMainItem.addItem("All courses", VaadinIcons.ACADEMY_CAP, createNavigationCommand("AdminAllCourses"));
		MenuItem addCoursesItem = myProfileMainItem.addItem("Add course", VaadinIcons.FILE_ADD, createNavigationCommand("AdminAddCourse"));
		MenuItem allOrdersItem = myProfileMainItem.addItem("All orders", VaadinIcons.NEWSPAPER, createNavigationCommand("AdminAllOrders"));
		MenuItem allUsersItem = myProfileMainItem.addItem("All users", VaadinIcons.USERS, createNavigationCommand("AdminAllUsers"));
		MenuItem addUserItem = myProfileMainItem.addItem("Add user", VaadinIcons.PLUS, createNavigationCommand("AdminAddUser"));
		MenuItem mySettingsItem = myProfileMainItem.addItem("Settings", VaadinIcons.USER, createNavigationCommand("Settings"));
		MenuItem myLogoutItem = myProfileMainItem.addItem("Log out", VaadinIcons.EXIT, createNavigationCommand("Home"));
		
		layoutH.addComponents(logoImage, profileMenu);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(profileMenu, Alignment.BOTTOM_RIGHT);
		
		// 2 - Search
		
		HorizontalLayout searchHLayout = new HorizontalLayout();
		TextField searchField = new TextField("");
		searchField.setPlaceholder("SEARCH by order ID");
		Button searchButton = new Button("Search", VaadinIcons.SEARCH);
		searchButton.addClickListener(e -> {
			Order selectedOrder = orderObj.getOrderById(Integer.parseInt(searchField.getValue()));
			grid.setItems(selectedOrder);
		});
		searchHLayout.addComponents(searchField, searchButton);
		searchHLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);
		
		
		// 3 - Result from the search order by ID:
				Label topLabel = new Label("Seach for a specific order by ID:");	
				
				
				grid.addColumn(com.academy.onlineAcademy.model.Order::getId).setCaption("Id");
				grid.addColumn(com.academy.onlineAcademy.model.Order::getCourseId).setCaption("Course ID");
				grid.addColumn(com.academy.onlineAcademy.model.Order::getUserId).setCaption("User ID");
				grid.addColumn(com.academy.onlineAcademy.model.Order::getPurchaseDate).setCaption("Purchase date");
				grid.addColumn(com.academy.onlineAcademy.model.Order::isPaid).setCaption("Paid?");
				grid.addColumn(com.academy.onlineAcademy.model.Order::getPrice).setCaption("Price");
				
				grid.setWidth("100%");
				grid.setHeight("100%");
				
				HorizontalLayout buttonsHLayout = new HorizontalLayout();
				buttonsHLayout.setVisible(false);
				Button deleteOrderButton = new Button("Delete", VaadinIcons.DEL);
				deleteOrderButton.setWidth("200px");
				Button updateOrderButton = new Button("Update", VaadinIcons.REFRESH);
				updateOrderButton.setWidth("200px");
				buttonsHLayout.addComponents(updateOrderButton, deleteOrderButton);
				
				grid.addItemClickListener(e -> {
					selectedOrder = e.getItem();
					selectedOrderId = selectedOrder.getId();
					System.out.println(selectedOrderId);
					buttonsHLayout.setVisible(true);
				});

				deleteOrderButton.addClickListener(e -> {
					try {
					orderObj.deleteOrderById(selectedOrderId);
			        orders = orderObj.getAllOrders();
			        grid.setItems(orders);
			        buttonsHLayout.setVisible(false);
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}
				});
				
				mainVLayout.addComponents(layoutH, searchHLayout, topLabel, grid, buttonsHLayout);
				mainVLayout.setComponentAlignment(searchHLayout, Alignment.MIDDLE_CENTER);
				mainVLayout.setComponentAlignment(buttonsHLayout, Alignment.BOTTOM_CENTER);
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
