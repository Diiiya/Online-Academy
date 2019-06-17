package com.academy.onlineAcademy.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AdminAddCourseView extends VerticalLayout implements View {
	
	public AdminAddCourseView() {
		
        VerticalLayout mainVLayout = new VerticalLayout();
		
		// 1 - Header bar ?
		HorizontalLayout layoutH = new HorizontalLayout();
		layoutH.setSpacing(true);
		layoutH.setWidth("100%");
		layoutH.setHeight("90px");
		
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
		
		// 2 - Add course panel
		VerticalLayout layoutVBody = new VerticalLayout();
		layoutVBody.setWidth("100%");
		
		Panel panel = new Panel("Add new course: ");
		panel.setSizeUndefined();
		
		TextField nameField = new TextField("Course name:");
		TextField descriptionField = new TextField("Course description:");
		TextField teacherNameField = new TextField("Teacher's  name:");
		TextField photoField = new TextField("Photo:");
		
		List<String> categories = Stream.of(Category.values())
                .map(Enum::name)
                .collect(Collectors.toList());
		
		ComboBox<String> selectCategoryComboBox = new ComboBox<>("Select category:", categories);
		selectCategoryComboBox.setEmptySelectionAllowed(false);
		
		List<String> levels = Stream.of(Level.values())
                .map(Enum::name)
                .collect(Collectors.toList());
		
		ComboBox<String> selectLevelComboBox = new ComboBox<>("Select level:", levels);
		selectLevelComboBox.setValue("BEGINNER");
		selectLevelComboBox.setEmptySelectionAllowed(false);
		
		List<String> givesCertificate = new ArrayList<>();
		givesCertificate.add("true");
		givesCertificate.add("false");
		ComboBox<String> selectCertComboBox = new ComboBox<>("Gives certificate:", givesCertificate);
		selectCertComboBox.setValue("true");
		selectCertComboBox.setEmptySelectionAllowed(false);
		
		TextField durationField = new TextField("Duration:");
		TextField priceField = new TextField("Price:");
		
		FormLayout content = new FormLayout();
		content.addComponents(nameField, descriptionField, teacherNameField, photoField, durationField, priceField,
				selectCertComboBox, selectCategoryComboBox, selectLevelComboBox);
		content.setSizeUndefined(); 
		content.setMargin(true);
		panel.setContent(content);
		
		//String basepath2 = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource coverResource = new FileResource(new File(basepath +
	            "/1online-courses_0.jpg"));
		
		Button addButton = new Button("ADD");
		addButton.setWidth("100");
		addButton.addClickListener(e -> {
			CourseController obj = new CourseController();
			//getClass().getResourceAsStream("")
			FileInputStream fileStream = null;
			
			try {
				fileStream = new FileInputStream(new File(basepath + "/1online-courses_0.jpg"));
				byte[] coverPhotoBytes = fileStream.readAllBytes();
				
				// Getting and converting String values from the UI to the required values for the Course constructor
				String name = nameField.getValue();
				int duration = Integer.parseInt(durationField.getValue());
				Level level = Level.valueOf(selectLevelComboBox.getValue());
				Category category = Category.valueOf(selectCategoryComboBox.getValue());
				double price = Double.parseDouble(priceField.getValue());
				Boolean cert = Boolean.parseBoolean(selectCertComboBox.getValue());
				obj.addCourse(name, descriptionField.getValue(), teacherNameField.getValue(), duration, level, category, price, cert, coverPhotoBytes);
				System.out.println(name);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			finally {
				if (fileStream != null) {
					try {
						fileStream.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		layoutVBody.addComponents(panel, addButton);
		layoutVBody.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		layoutVBody.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
		
        mainVLayout.addComponents(layoutH, layoutVBody);
        addComponent(mainVLayout);
        
	}

}
