package com.academy.onlineAcademy.view;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.helpView.UserViews;
import com.academy.onlineAcademy.helper.NewOrderMethods;
import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Level;
import com.academy.onlineAcademy.model.Course;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ImageRenderer;

public class HomeView extends VerticalLayout implements View {
	
	private static Logger logger = Logger.getLogger(HomeView.class.getName());
	private String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	
	private Navigator navigator;
	private Grid<Course> grid;
	private CourseController courseObj;
	private Button buyCourseButton;
	private Button loginButton;
	private Label topCoursesLabel;
	private TextField searchField;
	private Button searchButton;
	
	private Course selectedCourse;
	private List<Course> selectedCourses;
	private Course selected2Course;
	private UserViews userViews;
	
	// I18N fields
    private String country = new String();
    private String language = new String();
    private Locale ukLocale = new Locale("en","UK");
    private Locale frLocale = new Locale("fr","FR");
    private Locale esLocale = new Locale("es","ES");
    private Locale currentLocale = new Locale(language, country);
    private ResourceBundle bundle;
    
    // Grid columns:
    private Column<Course, String> courseNameCol;
    private Column<Course, String> courseDescCol;
    private Column<Course, String> courseTeacherCol;
    private Column<Course, Category> courseCategoryCol;
    private Column<Course, Integer> courseDurationCol;
    private Column<Course, Level> courseLevelCol;
    private Column<Course, Double> coursePriceCol;
    private Column<Course, Boolean> courseCertCol;
	
	/**
	 * Class constructor
	 */
	public HomeView() {
		
		navigator = UI.getCurrent().getNavigator();
		courseObj = new CourseController();
		userViews = new UserViews();
		initMainlayout();
		
	}
	
	/**
	 * Initializes the main layout
	 */
	private void initMainlayout() {
		
		VerticalLayout mainVLayout = new VerticalLayout();
		mainVLayout.setMargin(false);
		
		HorizontalLayout languagesBar = getLanguagesBar();
		HorizontalLayout layoutH = getRightTopBar();
		
		FileResource coverResource = new FileResource(new File(basepath + "/cover3.jpg"));
		Image coverImage = new Image("", coverResource);
		coverImage.setWidth("100%");
		coverImage.setHeight("350px");
		
		HorizontalLayout searchHLayout = getSearchLayout();
		topCoursesLabel = new Label("Top courses");
		grid = buildGrid();
		buyCourseButton = new Button("ADD");
		buyCourseButton.setVisible(false);
		buyCourseButton.addClickListener(e -> {
			int userId = getUserId();
			NewOrderMethods newOrderMethods = new NewOrderMethods();
			newOrderMethods.placeOrder(userId, selectedCourse);
			userViews.setLabelValue(userId);
		});
		
		mainVLayout.addComponents(languagesBar, layoutH, coverImage, searchHLayout, topCoursesLabel, grid, buyCourseButton);
		mainVLayout.setComponentAlignment(languagesBar, Alignment.TOP_RIGHT);
		mainVLayout.setComponentAlignment(searchHLayout, Alignment.MIDDLE_CENTER);
		addComponent(mainVLayout);
		
	}
	
	/**
	 * Creates a language bar for EN, ES and FR language selection
	 * @return HorizontalLayout
	 */
	private HorizontalLayout getLanguagesBar() {
		HorizontalLayout layoutH = new HorizontalLayout();
		
		FileResource ukResource = new FileResource(new File(basepath + "/united-kingdom.svg"));
		Image ukFlagImage = new Image("", ukResource);
		ukFlagImage.setWidth("30px");
		ukFlagImage.setHeight("30px");
		
		ukFlagImage.addClickListener(e -> {
			currentLocale = ukLocale;
			setNewValues();
		});
		
		FileResource spainResource = new FileResource(new File(basepath + "/spain.svg"));
		Image spainFlagImage = new Image("", spainResource);
		spainFlagImage.setWidth("30px");
		spainFlagImage.setHeight("30px");
		
		spainFlagImage.addClickListener(e -> {
			currentLocale = esLocale;
			setNewValues();
		});
		
		FileResource franceResource = new FileResource(new File(basepath + "/france.svg"));
		Image franceFlagImage = new Image("", franceResource);
		franceFlagImage.setWidth("30px");
		franceFlagImage.setHeight("30px");
		
		franceFlagImage.addClickListener(e -> {
			currentLocale = frLocale;
			setNewValues();
		});
		
		layoutH.addComponents(ukFlagImage, spainFlagImage, franceFlagImage);
		return layoutH;
	}
	
	/**
	 * Creates the top 
	 * @return HorizontalLayout
	 */
	private HorizontalLayout getTopBar() {
		HorizontalLayout layoutH = new HorizontalLayout();
		layoutH.setSpacing(true);
		layoutH.setWidth("100%");
		layoutH.setHeight("70px");
		
		HorizontalLayout miniLayoutH = new HorizontalLayout();
		
		FileResource logoResource = new FileResource(new File(basepath + "/art.svg"));
		Image logoImage = new Image("", logoResource);
		logoImage.setWidth("70px");
		logoImage.setHeight("70px");
		
		Label onlineAcademy = new Label("Online academy");
		
		loginButton = new Button("Login", VaadinIcons.SIGN_IN);
		loginButton.addClickListener(event -> navigator.navigateTo("Login"));
		
		miniLayoutH.addComponents(logoImage, onlineAcademy);
		miniLayoutH.setComponentAlignment(onlineAcademy, Alignment.MIDDLE_LEFT);
		layoutH.addComponents(miniLayoutH, loginButton);
		layoutH.setComponentAlignment(miniLayoutH, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(loginButton, Alignment.BOTTOM_RIGHT);
		
		return layoutH;
	}
	
	/**
	 * Creates the search course bar
	 * @return HorizontalLayout
	 */
	private HorizontalLayout getSearchLayout() {
		HorizontalLayout searchHLayout = new HorizontalLayout();
		
		searchField = new TextField("");
		searchField.setPlaceholder("SEARCH");
		searchButton = new Button("Search", VaadinIcons.SEARCH);
		
		searchButton.addClickListener(e -> {
//			try {
////				List<Course> selectedCourses = courseObj.getAllCourses();
////				grid.setItems(selectedCourses);
//				
//			}
//			catch(Exception ex) {
//				Notification notif = new Notification("Warning","No course with such name has been found!",
//					    Notification.TYPE_WARNING_MESSAGE);
//				notif.show(Page.getCurrent());
//				
//				logger.log(java.util.logging.Level.SEVERE, "No course with this name has been found!", ex);
//			}
			getCourses();
			if(getCourses() == true) {
				System.out.println("Should set courses");
				grid.setItems(selectedCourses);
				System.out.println("First course " + selectedCourses.get(0));
			}
			else if(getCourse() == true) {
				grid.setItems(selected2Course);
			}
			else {
			System.out.println("Should set courses");
			}

		});
		
		searchHLayout.addComponents(searchField, searchButton);
		searchHLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);
		
		return searchHLayout;
	}
	
	private boolean getCourses() {
		try {
			selectedCourses = courseObj.getCoursesByName(searchField.getValue().toUpperCase());
//			grid.setItems(selectedCourses);
			return true;
		}
		catch (Exception ex) {
			System.out.println("Exception - no courses found!");
		}
		return false;
	}
	
	private boolean getCourse() {
		try {
			selected2Course = courseObj.getCourseByName(searchField.getValue().toUpperCase());
//			grid.setItems(selectedCourse);
			return true;
		}
		catch(Exception ex) {
			Notification notif = new Notification("Warning","No course with such name has been found!",
				    Notification.TYPE_WARNING_MESSAGE);
			notif.show(Page.getCurrent());
			
			logger.log(java.util.logging.Level.SEVERE, "No course with this name has been found!", ex);
		}
		return false;
	}
	
	/**
	 * Creates the grid for the Course model
	 */
	private Grid<Course> buildGrid() {
		
		Grid<Course> grid = new Grid<>();
		List<Course> courses = courseObj.getAllCourses();
		grid.setItems(courses);		
		
		courseNameCol = grid.addColumn(com.academy.onlineAcademy.model.Course::getName).setCaption("Course name");
		
		courseDescCol = grid.addColumn(com.academy.onlineAcademy.model.Course::getDescription).setCaption("Course description");
		courseTeacherCol = grid.addColumn(com.academy.onlineAcademy.model.Course::getTeacherName).setCaption("Teacher");
		courseCategoryCol = grid.addColumn(com.academy.onlineAcademy.model.Course::getCategory).setCaption("Category");
		courseDurationCol = grid.addColumn(com.academy.onlineAcademy.model.Course::getDuration).setCaption("Duration");
		courseLevelCol = grid.addColumn(com.academy.onlineAcademy.model.Course::getLevel).setCaption("Level");
		coursePriceCol = grid.addColumn(com.academy.onlineAcademy.model.Course::getPrice).setCaption("Price in euros");
		courseCertCol = grid.addColumn(com.academy.onlineAcademy.model.Course::getGivesCertificate).setCaption("Gives certificate");
		
		grid.setWidth("100%");
		
		grid.addItemClickListener(e -> {
			buyCourseButton.setVisible(true);
			selectedCourse = e.getItem();
		});
		
		return grid;
	}
	
	/**
	 * Returns the right top bar - either for user or admin
	 * @return HorizontalLayout
	 */
	private HorizontalLayout getRightTopBar() {
		HorizontalLayout layoutH = new HorizontalLayout();
		int userId = getUserId();
		if (userId != 0) {
			System.out.println(" Home page ID :" + userId);
			
			layoutH = userViews.getTopBar(userId);
		}
		else {
			System.out.println("Doesn't go inside the else?!");
			layoutH = getTopBar();
		}
		return layoutH;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		getRightTopBar();
		
        List<Course> courses = courseObj.getAllCourses();
        grid.setItems(courses);
	}
	
	/**
	 * Gets the user id
	 * @return int - user id
	 */
	private int getUserId() {
		int userId;
		UI ui = UI.getCurrent();
		VaadinSession session = ui.getSession();
		if (session.getAttribute("user-id") != null) {
			userId = Integer.valueOf(String.valueOf(session.getAttribute("user-id")));
			System.out.println("USER ID :" + userId);
		}
		else {
			userId = 0;
			System.out.println("USER ID = 0");
		}
		return userId;
	}
	
	/**
	 * Sets new values to (translates) fields and buttons based on the selected language
	 */
	private void setNewValues() {
		bundle = ResourceBundle.getBundle("HomeViewMessagesBundle", currentLocale);
	
		topCoursesLabel.setValue(bundle.getString("topCoursesLabel"));
		loginButton.setCaption(bundle.getString("loginButton"));
		searchField.setPlaceholder(bundle.getString("search"));
		searchButton.setCaption(bundle.getString("search"));
		
		courseNameCol.setCaption(bundle.getString("courseNameCol"));
		courseDescCol.setCaption(bundle.getString("courseDescCol"));
		courseTeacherCol.setCaption(bundle.getString("teacherCol"));
		courseCategoryCol.setCaption(bundle.getString("categoryCol"));
		courseDurationCol.setCaption(bundle.getString("durationCol"));
		courseLevelCol.setCaption(bundle.getString("levelCol"));
		coursePriceCol.setCaption(bundle.getString("priceCol"));
		courseCertCol.setCaption(bundle.getString("certCol"));
	}
	


}
