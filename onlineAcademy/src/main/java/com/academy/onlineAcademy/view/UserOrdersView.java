package com.academy.onlineAcademy.view;

import java.awt.Dialog;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.academy.onlineAcademy.controller.OrderController;
import com.academy.onlineAcademy.helpView.UserViews;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Order;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class UserOrdersView extends VerticalLayout implements View {
	
	private static Logger logger = Logger.getLogger(UserOrdersView.class.getName());
	
	private Navigator navigator;
	private Label totalSumLabel = new Label();
	private Window confirmationWindow;
	private OrderController orderObj;
	private Grid<com.academy.onlineAcademy.model.Order> grid;
	private Button deleteButton;
	private double totalSum = 0;
	private Order selectedOrder;
	private int selectedOrderId;
	private int userId;
	private int itemsInCartCount;
	
	public UserOrdersView() {
		
		navigator = UI.getCurrent().getNavigator();
		orderObj = new OrderController();
//		initMainLayout();
		
	}
			
	private VerticalLayout initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		
		HorizontalLayout layoutH = UserViews.getTopBar(navigator, userId);
		Label myOrdersLabel = new Label("My orders:");
//		buildGrid();
		
		Button payButton = new Button("PAY");
		payButton.setWidth("200px");
		
		deleteButton = new Button("DELETE", VaadinIcons.DEL);
		deleteButton.setWidth("200px");
		deleteButton.setVisible(false);
		
		deleteButton.addClickListener(e -> confirmMessage());
		
		HorizontalLayout paymentHorizontalL = new HorizontalLayout();
		paymentHorizontalL = getPaymentHorizontalL();
		
		mainVLayout.addComponents(layoutH, myOrdersLabel, grid, deleteButton, paymentHorizontalL, payButton);
		mainVLayout.setComponentAlignment(deleteButton, Alignment.BOTTOM_RIGHT);
		mainVLayout.setComponentAlignment(paymentHorizontalL, Alignment.BOTTOM_RIGHT);
		mainVLayout.setComponentAlignment(payButton, Alignment.BOTTOM_RIGHT);
		addComponent(mainVLayout);
		
		return mainVLayout;
	}
	
	private void confirmMessage() {
		
		confirmationWindow = new Window("Sub-window");
		confirmationWindow.setClosable(false);
        VerticalLayout confirmationContent = new VerticalLayout();
        Label confirmationLabel = new Label("Are you sure you want to remove this course from the shopping cart before you proceed to check out?");
        
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        Button okayButton = new Button("Okay");
        okayButton.addClickListener(e -> deleteOrder());
        
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> UI.getCurrent().removeWindow(confirmationWindow));
        
        buttonsLayout.addComponents(okayButton, cancelButton);
        confirmationContent.addComponents(confirmationLabel, buttonsLayout);
        confirmationWindow.setContent(confirmationContent);
        
        confirmationWindow.center();
        
        UI.getCurrent().addWindow(confirmationWindow);
	}
	
	private void buildGrid() {
		
		grid = new Grid<>();
	    grid.setWidth("100%");
		
		grid.addColumn(com.academy.onlineAcademy.model.Order::getUserId).setCaption("User id");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getCourseId).setCaption("Course id");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getPurchaseDate).setCaption("Purchase date");
		grid.addColumn(com.academy.onlineAcademy.model.Order::isPaid).setCaption("Paid ?");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getPrice).setCaption("Price");
		
		grid.addItemClickListener(e -> {
			selectedOrder = e.getItem();
			selectedOrderId = selectedOrder.getId();
			deleteButton.setVisible(true);
		});
		
	}
	
	private HorizontalLayout getPaymentHorizontalL() {
		HorizontalLayout paymentHorizontalL = new HorizontalLayout();
		Label orderTotalSumlabel = new Label("Total:");
		paymentHorizontalL.addComponents(orderTotalSumlabel, totalSumLabel);
		
		return paymentHorizontalL;
	}
	
	private void deleteOrder() {
		orderObj.deleteOrderById(selectedOrderId);
		
		Notification notif = new Notification("Confirmation!", "Course successfully removed!", Notification.TYPE_WARNING_MESSAGE);
		notif.show(Page.getCurrent());
		
		logger.log(Level.INFO, "The course has been successfully removed!");
		
		UI.getCurrent().removeWindow(confirmationWindow);
		getAllTheOrdersOfTheUser(userId);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		buildGrid();
		UI ui = UI.getCurrent();
		VaadinSession session = ui.getSession();
		if (session.getAttribute("user-id") != null) {
			userId = Integer.valueOf(String.valueOf(session.getAttribute("user-id")));
			getAllTheOrdersOfTheUser(userId);
		}
		else {
			System.out.println("USER ID VAL:" + session.getAttribute("user-id"));
		}
		initMainLayout();
	}
	
	private void getAllTheOrdersOfTheUser(int userId) {
		try {
			List<Order> orders = orderObj.getAllOrdersByUser(userId);
			grid.setItems(orders);
			
			itemsInCartCount = orders.size();
			
			totalSum = 0;
			for (Order order : orders) {
				totalSum += order.getPrice();
			}
			
			totalSumLabel.setValue(String.valueOf(totalSum) + " euros");
		}
		catch(Exception ex) {
			Notification notif = new Notification("Warning", "No order(s) for this user have been found!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			
			logger.log(Level.SEVERE, "No order(s) for this user have been found!", ex);
		}
	}
	
}
