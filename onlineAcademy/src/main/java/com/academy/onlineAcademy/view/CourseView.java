package com.academy.onlineAcademy.view;

import java.text.SimpleDateFormat;
import java.util.Date;


import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.helpView.UserViews;
import com.academy.onlineAcademy.helper.InputSource;
import com.academy.onlineAcademy.model.Course;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class CourseView extends VerticalLayout implements View {
	
	private int userId;
	private int courseId;
	private Course course;
	private CourseController courseObj = new CourseController();
	
	private Label courseTitleLabel;
	private Label courseContentLabel;
	private Image courseImage;
	private VerticalLayout layoutVL;
	
	/**
	 * Class constructor
	 */
	public CourseView() {
		
		course = new Course();
		courseTitleLabel = new Label();
		courseContentLabel = new Label();
		courseImage = new Image();
		getAndDisplayCourse();
		initMainlayout();
		
	}
	
	/**
	 * Initializes the main layout
	 * @return VertiicalLayout
	 */
	private VerticalLayout initMainlayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		
		UserViews userViews = new UserViews();
		HorizontalLayout layoutH = userViews.getTopBar(userId);
		HorizontalLayout layoutBody = getLayoutBody();
		
		mainVLayout.addComponents(layoutH, layoutBody);
		addComponent(mainVLayout);
		
		return mainVLayout;
	}		
	
	/**
	 * Creates the body layout - course title, image and content
	 * @return HorizontalLayout
	 */
	private HorizontalLayout getLayoutBody() {
		HorizontalLayout layoutBody = new HorizontalLayout();
		layoutVL = new VerticalLayout();
		layoutVL.setWidth("1200px");
		courseImage.setWidth("600px");
		courseImage.setHeight("300px");
		layoutVL.addComponents(courseTitleLabel, courseImage, courseContentLabel);
		
		
		VerticalLayout layoutVR = new VerticalLayout();
		Label similarCoursesLabel = new Label("Similar courses:");
		layoutVR.setWidth("600px");
		layoutVR.addComponents(similarCoursesLabel);
		
		layoutBody.addComponents(layoutVL, layoutVR);
		
		return layoutBody;
	}

	/**
	 * Gets the selected course id from the session
	 */
	private void getSessionCourseId() {
		UI ui = UI.getCurrent();
		VaadinSession session = ui.getSession();
		if (session.getAttribute("course-id") != null) {
		    courseId = Integer.valueOf(String.valueOf(session.getAttribute("course-id")));
		}
	}
	
	/**
	 * Gets from the database and displays a course based on the course id in the session
	 */
	private void getAndDisplayCourse() {
		try {
			course = courseObj.getCourseById(courseId);
			courseTitleLabel.setValue(course.getName());
			StreamSource imagesource = new InputSource(course.getCoverPhoto());
			StreamResource imageResource = new StreamResource(imagesource, "CoverImage.jpg");
			courseImage.setSource(imageResource);
			imageResource.setFilename(generateResourceName());
			courseContentLabel.setValue(course.getDescription());
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Generates a random name (based on the time) for the image resource -> to update the cover image in the View (for every course)
	 * @return String
	 */
	private String generateResourceName() {
	    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	    String timestamp = df.format(new Date());
	    return "image_" + timestamp + ".png";
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
	
		getSessionCourseId();
		getAndDisplayCourse();
		
	}
	
}
