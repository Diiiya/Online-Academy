package com.academy.onlineAcademy;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

import javax.servlet.annotation.WebServlet;

import com.academy.onlineAcademy.view.AdminAddCourseView;
import com.academy.onlineAcademy.view.AdminAddUserView;
import com.academy.onlineAcademy.view.AdminAllCoursesView;
import com.academy.onlineAcademy.view.AdminAllOrdersView;
import com.academy.onlineAcademy.view.AdminAllUsersView;
import com.academy.onlineAcademy.view.CourseView;
import com.academy.onlineAcademy.view.HomeView;
import com.academy.onlineAcademy.view.LoginView;
import com.academy.onlineAcademy.view.SignUpView;
import com.academy.onlineAcademy.view.UserCoursesView;
import com.academy.onlineAcademy.view.UserOrdersView;
import com.academy.onlineAcademy.view.UserSettingsView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
// URLs without the #!
@PushStateNavigation
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        setSizeFull();
        layout.setSizeFull();
        layout.setMargin(false);
        
        Navigator navigator = new Navigator(this, layout);
        setNavigator(navigator);
        
        final LoginView loginViewObj = new LoginView();
        final SignUpView signUpViewObj = new SignUpView();
        final HomeView homeViewObj = new HomeView();
        final UserCoursesView userCoursesViewObj = new UserCoursesView();
        final CourseView courseViewObj = new CourseView();
        final UserOrdersView userOrdersViewObj = new UserOrdersView();
        final UserSettingsView userSettingsViewObj = new UserSettingsView();
        final AdminAddUserView adminAddUserViewObj = new AdminAddUserView();
        final AdminAllUsersView adminAllUsersObj = new AdminAllUsersView();
        final AdminAddCourseView adminAddCourseViewObj = new AdminAddCourseView();
        final AdminAllCoursesView adminAllCourseViewObj = new AdminAllCoursesView();
        final AdminAllOrdersView adminAllOrdersViewObj = new AdminAllOrdersView();
        
        navigator.addView("Login", loginViewObj);
        navigator.addView("SignUp", signUpViewObj);
        navigator.addView("", homeViewObj);
        navigator.addView("UserCourses", userCoursesViewObj);
        navigator.addView("Course", courseViewObj);
        navigator.addView("UserOrders", userOrdersViewObj);
        navigator.addView("Settings", userSettingsViewObj);
        navigator.addView("AdminAddUser", adminAddUserViewObj);
        navigator.addView("AdminAllUsers", adminAllUsersObj);
        navigator.addView("AdminAddCourse", adminAddCourseViewObj);
        navigator.addView("AdminAllCourses", adminAllCourseViewObj);
        navigator.addView("AdminAllOrders", adminAllOrdersViewObj);
        
        navigator.navigateTo("");
        
        setContent(layout);
        
//        configureLogging();
    }
    
//    private void configureLogging() {
//    	try {
//            LogManager.getLogManager().readConfiguration(new FileInputStream("logging.properties"));
//        } 
//    	catch (SecurityException | IOException e1) {
//            e1.printStackTrace();
//        }
//    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
