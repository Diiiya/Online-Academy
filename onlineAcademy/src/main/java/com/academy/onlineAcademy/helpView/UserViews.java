package com.academy.onlineAcademy.helpView;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

import com.academy.onlineAcademy.controller.OrderController;
import com.academy.onlineAcademy.helper.UserOrdersMethods;
import com.academy.onlineAcademy.model.Order;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.MenuItem;

public class UserViews extends VerticalLayout implements View {
	
	private Navigator navigator = UI.getCurrent().getNavigator();
	private Label itemsInCartCountLabel = new Label();
	private OrderController orderObj = new OrderController();
	private int itemsInCartCount = 0;	
	
	/**
	 * Counts the number of the orders for the logged in user
	 * @param userId
	 */
	private void getOrdersCountOfTheUser(int userId) {
		try {
			List<Order> orders = orderObj.getAllUnpaidOrdersByUser(userId);			
			itemsInCartCount = orders.size();
			
		}
		catch(Exception ex) {
		}
	}
	
	/**
	 * Sets the order count to the label in the top bar
	 * @param userId - the unique id of the logged in user
	 */
	public void setLabelValue(int userId) {
		getOrdersCountOfTheUser(userId);
		itemsInCartCountLabel.setValue(String.valueOf(itemsInCartCount));		
		System.out.println(" The label should already be equal to " + itemsInCartCount);
	}
	
	/**
	 * Creates the top bar for the user view - copmany's logo, number of orders in the basket and navigation menu
	 * @param userId
	 * @return top bar 
	 */
	public HorizontalLayout getTopBar(int userId) {
		HorizontalLayout layoutH = new HorizontalLayout();	
		layoutH.setSpacing(true);
		layoutH.setWidth("100%");
		layoutH.setHeight("70px");
		
		HorizontalLayout miniLayoutH = new HorizontalLayout();
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource logoResource = new FileResource(new File(basepath + "/art.svg"));
		Image logoImage = new Image("", logoResource);
		logoImage.setWidth("70px");
		logoImage.setHeight("70px");
		logoImage.addClickListener(e -> navigator.navigateTo(""));
		Label onlineAcademy = new Label("Online academy");
		
		HorizontalLayout rightHorizontalL = new HorizontalLayout();
		
		getOrdersCountOfTheUser(userId);
		itemsInCartCountLabel.setValue(String.valueOf(itemsInCartCount));
		FileResource shoppingBasketResource = new FileResource(new File(basepath + "/shop-cart.png"));
		Image shoppingBasketImage = new Image("", shoppingBasketResource);
		shoppingBasketImage.setHeight("50px");
		shoppingBasketImage.setWidth("50px");
		
		// MENU bar and methods to navigate to different pages	
		MenuBar profileMenu = new MenuBar();	
		MenuItem myProfileMainItem = profileMenu.addItem("My profile", VaadinIcons.MENU, null);
		myProfileMainItem.addItem("My courses", VaadinIcons.ACADEMY_CAP, createNavigationCommand("UserCourses"));
		myProfileMainItem.addItem("My orders", VaadinIcons.NEWSPAPER, createNavigationCommand("UserOrders"));
		myProfileMainItem.addItem("Settings", VaadinIcons.USER, createNavigationCommand("Settings"));
		myProfileMainItem.addItem("Log out", VaadinIcons.EXIT, logoutNavigationCommand(""));
		
		miniLayoutH.addComponents(logoImage, onlineAcademy);
		miniLayoutH.setComponentAlignment(onlineAcademy, Alignment.MIDDLE_LEFT);
		rightHorizontalL.addComponents(itemsInCartCountLabel, shoppingBasketImage, profileMenu);
		rightHorizontalL.setComponentAlignment(itemsInCartCountLabel, Alignment.BOTTOM_RIGHT);
		rightHorizontalL.setComponentAlignment(profileMenu, Alignment.BOTTOM_RIGHT);
		layoutH.addComponents(miniLayoutH, rightHorizontalL);
		layoutH.setComponentAlignment(rightHorizontalL, Alignment.MIDDLE_RIGHT);
		
		System.out.println(" Should return the right layout!!");
		return layoutH;
	}
	
	MenuBar.Command createNavigationCommand(String navigationView) {
		return new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
		    	System.out.print("LOG");
		    	navigator.navigateTo(navigationView);
		    }
		};
	}
	
	MenuBar.Command logoutNavigationCommand(String navigationView) {
		return new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
		    	UI ui = UI.getCurrent();
				VaadinSession session = ui.getSession();
				session.setAttribute("user-id", null);
		    	navigator.navigateTo(navigationView);
		    }
		};
	}

}
