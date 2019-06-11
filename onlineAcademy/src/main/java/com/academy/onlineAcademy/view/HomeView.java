package com.academy.onlineAcademy.view;

import java.io.File;

import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class HomeView extends VerticalLayout implements View {
	
	public HomeView() {
		
		VerticalLayout mainVLayout = new VerticalLayout();
		
		// 1 - Header bar ?
		HorizontalLayout layoutH = new HorizontalLayout ();
		layoutH.setWidth("100%");
		layoutH.setHeight("20px");
		
		Label logoLabel = new Label("LOGO");
		Button loginButton = new Button("LOGIN");
		loginButton.setWidth("150px");
		
		layoutH.addComponents(logoLabel, loginButton);
		layoutH.setComponentAlignment(logoLabel, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(loginButton, Alignment.TOP_RIGHT);
		
		// 2 - Cover IMAGE and Search
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource resource = new FileResource(new File(basepath +
	            "/1online-courses_0.jpg"));
			
		Image image = new Image("", resource);
		image.setWidth("100%");
		image.setHeight("200px");
		
		TextField searchField = new TextField("SEARCH");
		
		mainVLayout.addComponents(layoutH, image, searchField);
		mainVLayout.setComponentAlignment(searchField, Alignment.TOP_CENTER);
		addComponent(mainVLayout);
	}

}
