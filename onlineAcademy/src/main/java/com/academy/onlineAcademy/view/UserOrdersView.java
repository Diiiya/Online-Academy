package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.academy.onlineAcademy.model.Order;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class UserOrdersView extends VerticalLayout implements View {
	
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
		FileResource resource = new FileResource(new File(basepath +
	            "/user1.png"));
			
		Image logoImage = new Image("", logoResource);
		logoImage.setWidth("130px");
		logoImage.setHeight("60px");
		Image image = new Image("", resource);
		image.setWidth("50px");
		image.setHeight("50px");
		
		layoutH.addComponents(logoImage, image);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(image, Alignment.TOP_RIGHT);
		
		// 2 - Orders
		Label myOrdersLabel = new Label("My orders:");
		
		Date date = new Date();
		
		List<Order> orders = Arrays.asList(
				new Order(1, 1, 1, date, true, 50),
				new Order(2, 1, 2, date, true, 100),
				new Order(3, 1, 3, date, true, 300)
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
