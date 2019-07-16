package com.academy.onlineAcademy.view;

import java.io.File;

import com.academy.onlineAcademy.helpView.UserViews;
import com.academy.onlineAcademy.helper.ImageUploader;
import com.academy.onlineAcademy.helper.InputSource;
import com.academy.onlineAcademy.helper.UpdateUserMethods;
import com.academy.onlineAcademy.model.Person;
import com.academy.onlineAcademy.model.Type;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

public class UserSettingsView extends VerticalLayout implements View {
	
	private Binder<Person> binder;
	private UserViews userViews;
	private UpdateUserMethods updateUserMethods;
	private Person person;
	private String password = null;
	private String confirmPassword = null;
	private String enteredEmail;
	private int userId;
	private final Image image = new Image("Uploaded Image");
	private Image profileImage;
	private ImageUploader receiver;
	
	private Panel photoPanel;	
	private FormLayout photoContent;
	private Panel accountPanel;	
	private TextField fullNameField;
	private TextField emailField;
	private PasswordField passwordField;
	private PasswordField confirmPasswordField;
		
	public UserSettingsView() {
		
		updateUserMethods = new UpdateUserMethods();
		userViews = new UserViews();
		person = new Person();
		fullNameField = new TextField("Full name:");
		emailField = new TextField("Email:");
		passwordField = new PasswordField("Password:");
		confirmPasswordField = new PasswordField("Confirm password:");
		photoContent = new FormLayout();
		profileImage = new Image();
		
		initMainLayout();
				
	}
    
	private VerticalLayout initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		HorizontalLayout layoutH = userViews.getTopBar(userId);
		VerticalLayout layoutVBody = getBodyLayout();
		
//		binder = callBinder();
		
		mainVLayout.addComponents(layoutH, layoutVBody);
		addComponents(mainVLayout);
		
		return mainVLayout;
	}
					
	private VerticalLayout getBodyLayout() {
		VerticalLayout layoutVBody = new VerticalLayout();		
    	layoutVBody.setWidth("100%");
    	HorizontalLayout layoutHBody = new HorizontalLayout();
		layoutHBody.setSizeUndefined();
		getPhotoPanel();
		getAccountpanel();
		Button updateButton = new Button("UPDATE");
		
		updateButton.setWidth("800");
		updateButton.addClickListener(e -> {
			password = passwordField.getValue();
			confirmPassword = confirmPasswordField.getValue();
			File profileImageFile = receiver.getFile();
			updateUserMethods.updatePersonSettings(Type.USER, person, binder, enteredEmail, password, confirmPassword, profileImageFile);
//			boolean isSuccessful = getFieldsValues();
//			if (isSuccessful == true) {
//				File profileImageFile = receiver.getFile();
//				updateUserMethods.updatePersonSettings(Type.USER, person, binder, enteredEmail, password, confirmPassword, profileImageFile);
//			}
			
		});
		
		layoutHBody.addComponents(photoPanel, accountPanel);
		layoutHBody.setComponentAlignment(accountPanel, Alignment.MIDDLE_CENTER);

		layoutVBody.addComponents(layoutHBody, updateButton);
		layoutVBody.setComponentAlignment(layoutHBody, Alignment.MIDDLE_CENTER);
		layoutVBody.setComponentAlignment(updateButton, Alignment.MIDDLE_CENTER);
		
		return layoutVBody;
    }
	
//	private boolean getFieldsValues() {
//		enteredEmail = emailField.getValue();
//		enteredEmail.toUpperCase();
//		if (!passwordField.isEmpty() && passwordField.getValue() == person.getPassword()) {
//			return true;
//		}
//		else if (!passwordField.isEmpty() && (passwordField.getValue() != person.getPassword())) {
//			password = passwordField.getValue();
//			if (!confirmPasswordField.isEmpty()) {
//				confirmPassword = confirmPasswordField.getValue();
//				System.out.println("Confirm password: " + confirmPassword);
//				return true;
//			}
//			else {
//				Notification notif = new Notification("Warning", "Fill in the confirm password field to confirm it!",
//					    Notification.TYPE_WARNING_MESSAGE);
//				notif.show(Page.getCurrent());
//			}
//		}
//		return false;
//	}
	
	private Panel getPhotoPanel() {
		photoPanel = new Panel("Update my photo");
		image.setVisible(false);
		receiver = new ImageUploader(image);
		Upload uploadProfilePhoto = new Upload("", receiver);
		uploadProfilePhoto.addSucceededListener(receiver);
		image.setVisible(true);
		
		photoPanel.setSizeUndefined();
		photoPanel.setHeight("400px");
		profileImage.setWidth("150px");
		profileImage.setHeight("150px");
		image.setWidth("50px");
		image.setHeight("50px");
		
		photoContent.addComponents(profileImage, image, uploadProfilePhoto);
		photoContent.setComponentAlignment(profileImage, Alignment.TOP_CENTER);
		photoContent.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
		
		photoContent.setSizeUndefined(); 
		photoContent.setMargin(true);
		photoPanel.setContent(photoContent);
		
		return photoPanel;
	}
	
	private void setProfileImage() {
		try {
			StreamSource imageSource = new InputSource(person.getPhoto());
			StreamResource imageResource = new StreamResource(imageSource, "ProfileImage.jpg");
			profileImage.setSource(imageResource);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
			FileResource defaultImageResource = new FileResource(new File(basepath + "/user1.png"));
			profileImage.setSource(defaultImageResource);
		}
	}
	
	private Panel getAccountpanel() {
		accountPanel = new Panel("Update my account");
		FormLayout content = new FormLayout();
		
		accountPanel.setSizeUndefined();
		accountPanel.setHeight("400px");
		
		content.addComponents(fullNameField, emailField, passwordField, confirmPasswordField);
		content.setSizeUndefined(); 
		content.setMargin(true);
		accountPanel.setContent(content);
		
		return accountPanel;
	}
	
	private Binder<Person> callBinder() {
		binder = new Binder<Person>();
		
		binder.forField(fullNameField)
		.asRequired("Cannot be empty")
		.withValidator(new StringLengthValidator("Name must be between 5 and 30 characters long!",3, 50))
	    .bind(Person::getFullName, Person::setFullName);
		
		binder.forField(emailField)
		.asRequired("Cannot be empty")
		.withValidator(new EmailValidator("This doesn't seem to be a valid email address"))
		.withValidator(email -> email.length() <= 50, "Email address should be max 50 characters long!")
	    .bind(Person::getEmail, Person::setEmail);
		
		binder.forField(passwordField)
		.asRequired("Cannot be empty")
		.withValidator(new RegexpValidator("Password should contain at least one digit, one lower, one upper case letter and special symbol (# $ ^ + = ! * () @ % &) "
				+ "and be at least 8 characters long!", "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$^+=!*()@%&]).{8,30}$"))
		.bind(Person::getPassword, Person::setPassword);
		
		return binder;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		binder = callBinder();
		UI ui = UI.getCurrent();
		VaadinSession session = ui.getSession();
		if (session.getAttribute("user-id") != null) {
			userId = Integer.valueOf(String.valueOf(session.getAttribute("user-id")));
			updateUserMethods.getUserInfo(userId, binder);
			person = updateUserMethods.getPerson();
			userViews.setLabelValue(userId);
			setProfileImage();
		}
		else {
			System.out.println("USER ID VAL:" + session.getAttribute("user-id"));
		}
	}
	
}
