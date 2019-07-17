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
	
	private Logger logger = Logger.getLogger(UserOrdersView.class.getName());
	private List<Order> orders = new ArrayList<Order>();
	private OrderController orderObj = new OrderController();
	private Navigator navigator = UI.getCurrent().getNavigator();
	
	/**
	 * Validates the card details
	 * If valid - proceeds with the payment
	 * If not - shows a message
	 * @param binder
	 */
	public void validateCardDetails(Binder<CardDetails> binder) {
		if (binder.validate().isOk()) {
			makePayment();
//			List<Order> paidOrders = new ArrayList<Order>();
//			for (Order order : orders) {
//				order.setPaid(true);
//				orderObj.updateOrder(order);
//				paidOrders.add(order);
//			}
//			orders.removeAll(paidOrders);
//			
//			Notification notif = new Notification("Confirmation", "You have successfully made the payment!", Notification.TYPE_WARNING_MESSAGE);
//			notif.show(Page.getCurrent());
//			
//			navigator.navigateTo("UserCourses");
		}
		else {
			Notification notif = new Notification("Warning", "The entered payment details are not valid!", Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
		}
		
	}
	
	/**
	 * Sets the paid status of an order to true
	 * Adds the order to the list with paid orders
	 */
	private void makePayment() {
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
	
	/**
	 * Gets all the unpaid orders of the user from the database 
	 * @param userId
	 * @return List<Order> -- the unpaid orders for a specific user
 	 */
	public List<Order> getAllUnpaidOrdersOfTheUser(int userId) {
		try {
			orders = orderObj.getAllUnpaidOrdersByUser(userId);
			
			calculateTotalPrice();
		}
		catch(Exception ex) {
			
			logger.log(Level.SEVERE, "No order(s) for this user have been found!", ex);
		}
		return orders;
	}
	
	/**
	 * Calculates the total price of all courses added to the order
	 * @return double -  the total price of the order
	 */
	public double calculateTotalPrice() {
		double totalSum = 0;
		for (Order order : orders) {
			totalSum += order.getPrice();
		}
		
		return totalSum;
	}

}
