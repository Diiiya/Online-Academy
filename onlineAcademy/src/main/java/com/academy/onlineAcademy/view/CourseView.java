package com.academy.onlineAcademy.view;

import java.io.File;

import com.academy.onlineAcademy.helpView.UserViews;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class CourseView extends VerticalLayout implements View {
	
	Navigator navigator = UI.getCurrent().getNavigator();
	private String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	
	public CourseView() {
		
		initMainlayout();
		
	}
	
	public VerticalLayout initMainlayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		
		HorizontalLayout layoutH = UserViews.getTopBar(navigator);
		HorizontalLayout layoutBody = getLayoutBody();
		
		mainVLayout.addComponents(layoutH, layoutBody);
		addComponent(mainVLayout);
		
		return mainVLayout;
	}		
	
	public HorizontalLayout getLayoutBody() {
		HorizontalLayout layoutBody = new HorizontalLayout();
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
		
		
		VerticalLayout layoutVR = new VerticalLayout();
		Label similarCoursesLabel = new Label("Similar courses:");
		layoutVR.setWidth("600px");
		layoutVR.addComponents(similarCoursesLabel);
		
		layoutBody.addComponents(layoutVL, layoutVR);
		
		return layoutBody;
	}
	
}
