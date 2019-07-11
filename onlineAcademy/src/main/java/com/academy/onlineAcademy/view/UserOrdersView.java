package com.academy.onlineAcademy.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.academy.onlineAcademy.controller.CourseController;
import com.academy.onlineAcademy.controller.OrderController;
import com.academy.onlineAcademy.helpView.UserViews;
import com.academy.onlineAcademy.helper.UserOrdersMethods;
import com.academy.onlineAcademy.model.CardDetails;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Order;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.SingleSelectionModel;

public class UserOrdersView extends VerticalLayout implements View {
	
	private static Logger logger = Logger.getLogger(UserOrdersView.class.getName());
	
	private Label totalSumLabel1 = new Label();
	private Window confirmationWindow;
	private OrderController orderObj;
	private Grid<com.academy.onlineAcademy.model.Order> grid;
	private Button deleteButton;
	private double totalSum;
	private Order selectedOrder;
	private int selectedOrderId;
	private int userId;
	private Window paymentWindow;
	
	private TextField cardNumField;
	private DateField expDateField;
	private PasswordField secCodeField;
	private Binder<CardDetails> binder;
	
	private List<Order> orders;
	private UserViews userViews;
	private UserOrdersMethods userOrdersMethods;
	
	public UserOrdersView() {
		
		userViews = new UserViews();
		userOrdersMethods = new UserOrdersMethods();
		orderObj = new OrderController();
		cardNumField = new TextField("Card number:");
		expDateField = new DateField("Expiry date:");
		secCodeField = new PasswordField("Security code:");
		orders = new ArrayList<Order>();
		
		initMainLayout();
		
	}
			
	private VerticalLayout initMainLayout() {
		VerticalLayout mainVLayout = new VerticalLayout();
		
		
		HorizontalLayout layoutH = userViews.getTopBar(userId);
		Label myOrdersLabel = new Label("My orders:");
		buildGrid();
		Button payButton = new Button("PAY");
		payButton.setWidth("200px");
		payButton.addClickListener(e -> {
			createPaymentWindow();
			callBinder();
			paymentWindow.setVisible(true);
		});
		
		
		deleteButton = new Button("DELETE", VaadinIcons.DEL);
		deleteButton.setWidth("200px");
		deleteButton.setVisible(false);
		
		deleteButton.addClickListener(e -> confirmMessage());
		
		HorizontalLayout paymentHorizontalL = new HorizontalLayout();
		paymentHorizontalL = getPaymentHorizontalL();
		
		mainVLayout.addComponents(layoutH, myOrdersLabel, grid, deleteButton, paymentHorizontalL, payButton);
		mainVLayout.setComponentAlignment(deleteButton, Alignment.BOTTOM_RIGHT);
		mainVLayout.setComponentAlignment(paymentHorizontalL, Alignment.BOTTOM_RIGHT);
		mainVLayout.setComponentAlignment(payButton, Alignment.BOTTOM_RIGHT);
		addComponent(mainVLayout);
		
		return mainVLayout;
	}
	
	private void confirmMessage() {
		
		confirmationWindow = new Window("Sub-window");
		confirmationWindow.setClosable(false);
        VerticalLayout confirmationContent = new VerticalLayout();
        Label confirmationLabel = new Label("Are you sure you want to remove this course from the shopping cart before you proceed to check out?");
        
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        Button okayButton = new Button("Okay");
        okayButton.addClickListener(e -> deleteOrder());
        
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> UI.getCurrent().removeWindow(confirmationWindow));
        
        buttonsLayout.addComponents(okayButton, cancelButton);
        confirmationContent.addComponents(confirmationLabel, buttonsLayout);
        confirmationWindow.setContent(confirmationContent);
        
        confirmationWindow.center();
        
        UI.getCurrent().addWindow(confirmationWindow);
	}
	
	private void buildGrid() {
		
		grid = new Grid<>();
	    grid.setWidth("100%");
		
		grid.addColumn(com.academy.onlineAcademy.model.Order::getUserId).setCaption("User id");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getCourseId).setCaption("Course id");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getPurchaseDate).setCaption("Purchase date");
		grid.addColumn(com.academy.onlineAcademy.model.Order::isPaid).setCaption("Paid ?");
		grid.addColumn(com.academy.onlineAcademy.model.Order::getPrice).setCaption("Price");
		
		grid.addItemClickListener(e -> {
			selectedOrder = e.getItem();
			selectedOrderId = selectedOrder.getId();
			deleteButton.setVisible(true);
		});
		
		
	}
	
	private HorizontalLayout getPaymentHorizontalL() {
		HorizontalLayout paymentHorizontalL = new HorizontalLayout();
		Label orderTotalSumlabel = new Label("Total:");
		paymentHorizontalL.addComponents(orderTotalSumlabel, totalSumLabel1);
		
		return paymentHorizontalL;
	}
	
	private void deleteOrder() {
		orderObj.deleteOrderById(selectedOrderId);
		
		Notification notif = new Notification("Confirmation!", "Course successfully removed!", Notification.TYPE_WARNING_MESSAGE);
		notif.show(Page.getCurrent());
		
		logger.log(Level.INFO, "The course has been successfully removed!");
		
		UI.getCurrent().removeWindow(confirmationWindow);
		
		orders = userOrdersMethods.getAllUnpaidOrdersOfTheUser(userId);
		////////////
		grid.setItems(orders);
		totalSum = userOrdersMethods.calculateTotalPrice();
		totalSumLabel1.setValue(String.valueOf(totalSum) + " euros");
		userViews.setLabelValue(userId);
	}
	
	private void createPaymentWindow() {
		paymentWindow = new Window("Payment details");
		paymentWindow.setVisible(false);
		
		Label totalSumLabel2 = new Label("Total price: " + String.valueOf(totalSum));
		Button confirmPaymentDetailsButton = new Button("Make payment");
		confirmPaymentDetailsButton.setWidth("200px");
		confirmPaymentDetailsButton.addClickListener(e -> userOrdersMethods.validateCardDetails(binder));
		
		FormLayout content = new FormLayout();
		expDateField.setDateFormat("MM/yy");
		content.addComponents(cardNumField, expDateField, secCodeField,  totalSumLabel2, confirmPaymentDetailsButton);
		content.setSizeUndefined();
		content.setMargin(true);
		
		paymentWindow.setContent(content);
		paymentWindow.center();
		UI.getCurrent().addWindow(paymentWindow);
	}
	
	private void callBinder() {
		
		binder = new Binder<>();
		LocalDate dateToday =  LocalDate.now();
		
		binder.forField(cardNumField)
		.asRequired("Cannot be empty")
//		.withValidator(new RegexpValidator("Not a valid card number!", "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|\" +\r\n" + 
//				"        \"(?<mastercard>5[1-5][0-9]{14})|\" +\r\n" + 
//				"        \"(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|\" +\r\n" + 
//				"        \"(?<amex>3[47][0-9]{13})|\" +\r\n" + 
//				"        \"(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|\" +\r\n" + 
//				"        \"(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$"))
		.withConverter(new StringToIntegerConverter("Must enter a number"))
	    .bind(CardDetails::getCardNumber, CardDetails::setCardNumber);
		
		binder.forField(expDateField)
		.asRequired("Cannot be empty")
		.withValidator(date -> date.isAfter(dateToday) , "The card has already expired!")
		.bind(CardDetails::getExpiryDate, CardDetails::setExpiryDate);
		
		binder.forField(secCodeField)
		.asRequired("Cannot be empty")
		.withConverter(new StringToIntegerConverter("Must enter a number"))
		.withValidator(code -> String.valueOf(code).length() == 3, "The security code is 3-digit long")
		.bind(CardDetails::getSecCode, CardDetails::setSecCode);
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		UI ui = UI.getCurrent();
		VaadinSession session = ui.getSession();
		if (session.getAttribute("user-id") != null) {
			userId = Integer.valueOf(String.valueOf(session.getAttribute("user-id")));
			orders = userOrdersMethods.getAllUnpaidOrdersOfTheUser(userId);
			grid.setItems(orders);
			totalSum = userOrdersMethods.calculateTotalPrice();
			totalSumLabel1.setValue(String.valueOf(totalSum) + " euros");
//			System.out.println("NEW USER ID ~ " + userId);
			userViews.setLabelValue(userId);
			///////////////////////////////////////////////////
			// label.setValue to change the coursesCount !!! //
			///////////////////////////////////////////////////
		}
		else {
			System.out.println("USER ID VAL:" + session.getAttribute("user-id"));
		}
	}
	
}
