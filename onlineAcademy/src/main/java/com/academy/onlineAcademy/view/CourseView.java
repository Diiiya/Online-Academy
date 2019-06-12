package com.academy.onlineAcademy.view;

import java.io.File;

import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class CourseView extends VerticalLayout implements View {
	
	public CourseView() {
		
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
		
		// 2 - Body
		HorizontalLayout layoutBody = new HorizontalLayout();
		// 2.1 - Course content - Left bar
		VerticalLayout layoutVL = new VerticalLayout();
		layoutVL.setWidth("1200px");
		
		Label courseTitleLabel = new Label("COURSE TITLE");
		
		FileResource courseImageResource = new FileResource(new File(basepath +
	            "/1online-courses_0.jpg"));
		Image courseImage = new Image("", courseImageResource);
		courseImage.setWidth("600px");
		courseImage.setHeight("300px");
		
		Label courseContentLabel = new Label("Course Content Here ...........");
		
		layoutVL.addComponents(courseTitleLabel, courseImage, courseContentLabel);
		
		// 2.2 - Similar courses - Right bar
		VerticalLayout layoutVR = new VerticalLayout();
		layoutVR.setWidth("600px");
		
		Label similarCoursesLabel = new Label("Similar courses:");
		
		layoutVR.addComponents(similarCoursesLabel);
		
		
		layoutBody.addComponents(layoutVL, layoutVR);
		
		mainVLayout.addComponents(layoutH, layoutBody);
		addComponent(mainVLayout);
	}

}
