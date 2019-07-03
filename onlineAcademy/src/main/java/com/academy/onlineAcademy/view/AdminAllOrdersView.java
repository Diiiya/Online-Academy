package com.academy.onlineAcademy.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.controller.OrderController;
import com.academy.onlineAcademy.controller.PersonController;
import com.academy.onlineAcademy.exceptions.LoginException;
import com.academy.onlineAcademy.exceptions.OrderException;
import com.academy.onlineAcademy.helpView.AdminViews;
import com.academy.onlineAcademy.helper.NewOrderMethods;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Order;
import com.academy.onlineAcademy.model.Person;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class AdminAllOrdersView extends VerticalLayout implements View {
	
	private Navigator navigator;
	private HorizontalLayout buttonsHLayout;	
	private TextField searchField;
	private TextField customerEmailField;
	private OrderController orderObj;
	private CourseController courseObj;
	private PersonController personObj;
	private Grid<com.academy.onlineAcademy.model.Order> grid;
	private int selectedOrderId;
	private ComboBox<String> selectCourseCB;
	
	public AdminAllOrdersView() {
		
		navigator = UI.getCurrent().getNavigator();
		orderObj = new OrderController();
		courseObj = new CourseController();
		personObj = new PersonController();
		initMainLayout();
	
	}
	
	public VerticalLayout initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		mainVLayout.setHeight("100%");
		
		HorizontalLayout layoutH = AdminViews.getTopBar(navigator);
		HorizontalLayout searchHLayout = getSearchLayout();
		Label topLabel = new Label("Seach for a specific order by ID:");
		buildGrid();
		buttonsHLayout = getDELIUPDButtonsLayout();
		HorizontalLayout addOrderLayout = getAddOrderLayout();
		
		mainVLayout.addComponents(layoutH, searchHLayout, topLabel, grid, buttonsHLayout, addOrderLayout);
		mainVLayout.setComponentAlignment(searchHLayout, Alignment.MIDDLE_CENTER);
		mainVLayout.setComponentAlignment(buttonsHLayout, Alignment.BOTTOM_CENTER);
		addComponent(mainVLayout);
		
		return mainVLayout;
	}
	
	public HorizontalLayout getSearchLayout() {
		
		HorizontalLayout searchHLayout = new HorizontalLayout();
		
		searchField = new TextField("");
		searchField.setPlaceholder("SEARCH by order ID");
		Button searchButton = new Button("Search", VaadinIcons.SEARCH);
		searchButton.addClickListener(e -> lookForOrderById());
		
		searchHLayout.addComponents(searchField, searchButton);
		searchHLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);
		
		return searchHLayout;
		
	}
	
	public HorizontalLayout getDELIUPDButtonsLayout() {
		buttonsHLayout = new HorizontalLayout();
		
		buttonsHLayout.setVisible(false);
		Button deleteOrderButton = new Button("Delete", VaadinIcons.DEL);
		deleteOrderButton.setWidth("200px");
//		Button updateOrderButton = new Button("Update", VaadinIcons.REFRESH);
//		updateOrderButton.setWidth("200px");
		
		deleteOrderButton.addClickListener(e -> deleteOrderById());
		
		buttonsHLayout.addComponents(/*updateOrderButton, */ deleteOrderButton);
		return buttonsHLayout;
	}
	
	public HorizontalLayout getAddOrderLayout() {
		HorizontalLayout addOrderLayout = new HorizontalLayout();
		
		selectCourseCB = new ComboBox<>("Select a course");
		
		List<String> courseNames = NewOrderMethods.getCourses(courseObj);
		selectCourseCB.setItems(courseNames);
		
		customerEmailField = new TextField("User email: ");
		Button orderButton = new Button("MAKE ORDER");
		orderButton.addClickListener(e -> order());
		
		addOrderLayout.addComponents(selectCourseCB, customerEmailField, orderButton);
		addOrderLayout.setComponentAlignment(orderButton, Alignment.BOTTOM_RIGHT);
		
		return addOrderLayout;
	}
	
	public void buildGrid() {
		grid = new Grid<>();
		
		grid.addColumn(com.academy.onlineAcademy.model.Order::getId).setCaption("Id");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getCourseId).setCaption("Course ID");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getUserId).setCaption("User ID");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getPurchaseDate).setCaption("Purchase date");
		grid.addColumn(com.academy.onlineAcademy.model.Order::isPaid).setCaption("Paid?");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getPrice).setCaption("Price");
		
		grid.setWidth("100%");
		grid.setHeight("50%");
		
		grid.addItemClickListener(e -> {
			Order selectedOrder = e.getItem();
			selectedOrderId = selectedOrder.getId();
			System.out.println(selectedOrderId);
			buttonsHLayout.setVisible(true);
		});

	}
	
	public void lookForOrderById() {
		try {
			Order selectedOrder = orderObj.getOrderById(Integer.parseInt(searchField.getValue()));
			grid.setItems(selectedOrder);
		}
		catch(Exception ex) {
			Notification notif = new Notification("Warning", "There was no order with this id found!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
	}
	
	public void deleteOrderById() {
		try {
			orderObj.deleteOrderById(selectedOrderId);
	        List<Order> orders = orderObj.getAllOrders();
	        grid.setItems(orders);
	        buttonsHLayout.setVisible(false);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void order() {
		String courseName = selectCourseCB.getValue();
		String userEmail = customerEmailField.getValue();
		if (courseName != null) {
			NewOrderMethods.placeOrder(userEmail, personObj, orderObj, courseName, courseObj);
		}
		else {
			Notification notif = new Notification("Warning!", "Both fields should be filled in",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
	}
	
}
