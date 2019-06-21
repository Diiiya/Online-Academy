package com.academy.onlineAcademy.view;

import java.io.File;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.MenuItem;

public class UserSettingsView extends VerticalLayout implements View {
	
	Navigator navigator = UI.getCurrent().getNavigator();
	
	public UserSettingsView() {
		
		VerticalLayout mainVLayout = new VerticalLayout();
		
		// 1 - Header bar ?
		HorizontalLayout layoutH = new HorizontalLayout();
		layoutH.setSpacing(true);
		layoutH.setWidth("100%");
		layoutH.setHeight("90px");
		
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

		// 2 - Photo panel
		VerticalLayout layoutVBody = new VerticalLayout();
		layoutVBody.setWidth("100%");
		
		HorizontalLayout layoutHBody = new HorizontalLayout();
		layoutHBody.setSizeUndefined();
		
				Panel photoPanel = new Panel("Update my photo");
				photoPanel.setSizeUndefined();
				photoPanel.setHeight("400px");
				FormLayout photoContent = new FormLayout();
				
				FileResource profileImageResource = new FileResource(new File(basepath +
			            "/1024px-Circle-icons-profile.svg.png"));
					
				Image profileImage = new Image("", profileImageResource);
				profileImage.setWidth("200px");
				profileImage.setHeight("200px");
				
				photoContent.addComponent(profileImage);
				photoContent.setComponentAlignment(profileImage, Alignment.MIDDLE_CENTER);
				HorizontalLayout layoutHsmall = new HorizontalLayout();
				TextField photoLabel = new TextField("Photo");
				Button photoAddButton = new Button("Add image");
				layoutHsmall.addComponents(photoLabel, photoAddButton);
				layoutHsmall.setComponentAlignment(photoAddButton, Alignment.BOTTOM_RIGHT);
				photoContent.addComponent(layoutHsmall);
				
				photoContent.setSizeUndefined(); 
				photoContent.setMargin(true);
				photoPanel.setContent(photoContent);
				
				// 2 - Update panel
				Panel accountPanel = new Panel("Update my account");
				accountPanel.setSizeUndefined();
				accountPanel.setHeight("400px");
				
				TextField fullNameField = new TextField("Full name:");
				TextField emailField = new TextField("Email:");
				PasswordField passwordField = new PasswordField("Password:");
				PasswordField confirmPasswordField = new PasswordField("Confirm password");
				
				FormLayout content = new FormLayout();
				content.addComponents(fullNameField, emailField, passwordField, confirmPasswordField);
				content.setSizeUndefined(); 
				content.setMargin(true);
				accountPanel.setContent(content);
				
				Button addButton = new Button("UPDATE");
				addButton.setWidth("100");
				
				layoutHBody.addComponents(photoPanel, accountPanel);
				layoutHBody.setComponentAlignment(accountPanel, Alignment.MIDDLE_CENTER);
		
				layoutVBody.addComponents(layoutHBody, addButton);
				layoutVBody.setComponentAlignment(layoutHBody, Alignment.MIDDLE_CENTER);
				layoutVBody.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
				
		mainVLayout.addComponents(layoutH, layoutVBody);
		addComponents(mainVLayout);
	}

}
