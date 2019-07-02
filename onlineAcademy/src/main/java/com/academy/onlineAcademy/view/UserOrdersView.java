package com.academy.onlineAcademy.view;

import java.util.List;

import com.academy.onlineAcademy.controller.OrderController;
import com.academy.onlineAcademy.helpView.UserViews;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Order;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class UserOrdersView extends VerticalLayout implements View {
	
	private Navigator navigator;
	private OrderController orderObj;
	private Grid<com.academy.onlineAcademy.model.Order> grid;
	
	public UserOrdersView() {
		
		navigator = UI.getCurrent().getNavigator();
		orderObj = new OrderController();
		initMainLayout();
		
	}
			
	public VerticalLayout initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		
		HorizontalLayout layoutH = UserViews.getTopBar(navigator);
		Label myOrdersLabel = new Label("My orders:");
		buildGrid();
		
		mainVLayout.addComponents(layoutH, myOrdersLabel, grid);
		addComponent(mainVLayout);
		
		return mainVLayout;
	}
	
	public void buildGrid() {
		
		grid = new Grid<>();
	    grid.setWidth("100%");
		
		grid.addColumn(com.academy.onlineAcademy.model.Order::getUserId).setCaption("User id");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getCourseId).setCaption("Course id");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getPurchaseDate).setCaption("Purchase date");
		grid.addColumn(com.academy.onlineAcademy.model.Order::isPaid).setCaption("Paid ?");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getPrice).setCaption("Price");
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		UI ui = UI.getCurrent();
		VaadinSession session = ui.getSession();
		if (session.getAttribute("user-id") != null) {
			int userId = Integer.valueOf(String.valueOf(session.getAttribute("user-id")));
			getAllTheOrdersOfTheUser(userId);
		}
		else {
			System.out.println("USER ID VAL:" + session.getAttribute("user-id"));
		}
	}
	
	public void getAllTheOrdersOfTheUser(int userId) {
		try {
			List<Order> orders = orderObj.getAllOrdersByUser(userId);
			grid.setItems(orders);
		}
		catch(Exception ex) {
			Notification notif = new Notification(
				    "Warning",
				    "No order(s) for this user have been found!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
	}
	
}
