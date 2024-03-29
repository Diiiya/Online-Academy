package com.academy.onlineAcademy.helpView;

import java.io.File;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.MenuItem;

public class AdminViews extends VerticalLayout implements View {
	
	private Navigator navigator = UI.getCurrent().getNavigator();
	private String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	
	/**
	 * Creates the top bar for the Admin view - company's logo and menu
	 * @param navigator
	 * @return the top bar
	 */
	public HorizontalLayout getTopBar(Navigator navigator) {
		HorizontalLayout layoutH = new HorizontalLayout();
		layoutH.setSpacing(true);
		layoutH.setWidth("100%");
		layoutH.setHeight("90px");
		
		HorizontalLayout miniLayoutH = new HorizontalLayout();
		FileResource logoResource = new FileResource(new File(basepath + "/art.svg"));
		Image logoImage = new Image("", logoResource);
		logoImage.setWidth("70px");
		logoImage.setHeight("70px");
		Label onlineAcademy = new Label("Online academy");
		
		// MENU bar and methods to navigate to different pages
		MenuBar profileMenu = new MenuBar();
		MenuItem myProfileMainItem = profileMenu.addItem("My profile", VaadinIcons.MENU, null);
		myProfileMainItem.addItem("All courses", VaadinIcons.ACADEMY_CAP, createNavigationCommand("AdminAllCourses"));
		myProfileMainItem.addItem("Add course", VaadinIcons.FILE_ADD, createNavigationCommand("AdminAddCourse"));
		myProfileMainItem.addItem("All orders", VaadinIcons.NEWSPAPER, createNavigationCommand("AdminAllOrders"));
		myProfileMainItem.addItem("All users", VaadinIcons.USERS, createNavigationCommand("AdminAllUsers"));
		myProfileMainItem.addItem("Add user", VaadinIcons.PLUS, createNavigationCommand("AdminAddUser"));
		myProfileMainItem.addItem("Settings", VaadinIcons.USER, createNavigationCommand("Settings"));
		myProfileMainItem.addItem("Log out", VaadinIcons.EXIT, logoutNavigationCommand(""));
		
		miniLayoutH.addComponents(logoImage, onlineAcademy);
		miniLayoutH.setComponentAlignment(onlineAcademy, Alignment.MIDDLE_LEFT);
		layoutH.addComponents(miniLayoutH, profileMenu);
		layoutH.setComponentAlignment(profileMenu, Alignment.BOTTOM_RIGHT);
		
		return layoutH;
	}
	
	MenuBar.Command createNavigationCommand(String navigationView) {
		return new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
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
