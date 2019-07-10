package com.academy.onlineAcademy.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.academy.onlineAcademy.controller.OrderController;
import com.academy.onlineAcademy.model.CardDetails;
import com.academy.onlineAcademy.model.Order;
import com.academy.onlineAcademy.view.UserOrdersView;
import com.vaadin.data.Binder;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class UserOrdersMethods {
	
	private static Logger logger = Logger.getLogger(UserOrdersView.class.getName());
	private static List<Order> orders = new ArrayList<Order>();
	private static OrderController orderObj = new OrderController();
	private static Navigator navigator = UI.getCurrent().getNavigator();
	
	public static void validateCardDetails(Binder<CardDetails> binder) {
		if (binder.validate().isOk()) {
			List<Order> paidOrders = new ArrayList<Order>();
			for (Order order : orders) {
				order.setPaid(true);
				orderObj.updateOrder(order);
				paidOrders.add(order);
			}
			orders.removeAll(paidOrders);
			
			Notification notif = new Notification("Confirmation", "You have successfully made the payment!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			
			navigator.navigateTo("UserCourses");
		}
		else {
			Notification notif = new Notification("Warning", "The entered payment details are not valid!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
		
	}
	
	public static List<Order> getAllUnpaidOrdersOfTheUser(int userId) {
		try {
			orders = orderObj.getAllUnpaidOrdersByUser(userId);
//			grid.setItems(orders);
			
			calculateTotalPrice();
		}
		catch(Exception ex) {
//			Notification notif = new Notification("Warning", "No order(s) for this user have been found!",
//				    Notification.TYPE_WARNING_MESSAGE);
//			notif.show(Page.getCurrent());
			
			logger.log(Level.SEVERE, "No order(s) for this user have been found!", ex);
		}
		return orders;
	}
	
	public static double calculateTotalPrice() {
		double totalSum = 0;
		for (Order order : orders) {
			totalSum += order.getPrice();
		}
		
		return totalSum;
	}

}
