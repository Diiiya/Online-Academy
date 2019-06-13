package com.academy.onlineAcademy;

import javax.servlet.annotation.WebServlet;

import com.academy.onlineAcademy.view.AdminAddCourseView;
import com.academy.onlineAcademy.view.AdminAddView;
import com.academy.onlineAcademy.view.CourseView;
import com.academy.onlineAcademy.view.HomeView;
import com.academy.onlineAcademy.view.LoginView;
import com.academy.onlineAcademy.view.UserCoursesView;
import com.academy.onlineAcademy.view.UserOrdersView;
import com.academy.onlineAcademy.view.UserSettingsView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
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
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        setSizeFull();
        layout.setSizeFull();
        layout.setMargin(false);
        
        Navigator navigator = new Navigator(this, layout);
        setNavigator(navigator);
        
        LoginView loginViewObj = new LoginView();
        HomeView homeViewObj = new HomeView();
        UserCoursesView userCoursesViewObj = new UserCoursesView();
        CourseView courseViewObj = new CourseView();
        UserOrdersView userOrdersViewObj = new UserOrdersView();
        UserSettingsView userSettingsViewObj = new UserSettingsView();
        AdminAddView adminAddViewObj = new AdminAddView();
        AdminAddCourseView adminAddCourseViewObj = new AdminAddCourseView();
        
        navigator.addView("Login", loginViewObj);
        navigator.addView("Home", homeViewObj);
        navigator.addView("UserCourses", userCoursesViewObj);
        navigator.addView("Course", courseViewObj);
        navigator.addView("UserOrders", userOrdersViewObj);
        navigator.addView("Settings", userSettingsViewObj);
        navigator.addView("AdminAdd", adminAddViewObj);
        navigator.addView("AdminAddCourse", adminAddCourseViewObj);
        
        navigator.navigateTo("Home");
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
