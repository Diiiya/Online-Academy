package com.academy.onlineAcademy.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Order;
import com.academy.onlineAcademy.model.Person;
import com.academy.onlineAcademy.model.Type;

public class JPAOrder {

	private static final EntityManagerFactory emFactoryObj;
    private static final String PERSISTENCE_UNIT_NAME = "personPersistence";  
 
    static {
        emFactoryObj = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
 
    // This Method Is Used To Retrieve The 'EntityManager' Object
    public static EntityManager getEntityManager() {
        return emFactoryObj.createEntityManager();
    }

	public static void main(String[] args) {
//        EntityManager entityMgr = getEntityManager();
//        entityMgr.getTransaction().begin();
// 
////        
//        Date date = new Date();
////        Order order = new Order(1, 8, date, true, 110);
//        Order order1 = new Order(122, 8, date, true, 50);
//        Order order2 = new Order(122, 7, date, true, 150);
//        Order order3 = new Order(122, 19, date, true, 50);
////        entityMgr.persist(order);
//        entityMgr.persist(order1);
//        entityMgr.persist(order2);
//        entityMgr.persist(order3);
//// 
//        
//        entityMgr.getTransaction().commit();
// 
//        entityMgr.clear();
//        System.out.println("Record Successfully Inserted In The Database");
		
		OrderController orderObj = new OrderController();
		
		try {
			Order order = orderObj.getOrderById(2);
			System.out.println(order.getUserId());
//			List<Order> orders = orderObj.getAllOrdersByUser(122);
//			System.out.println(orders.get(0));
//			List<Order> orders = orderObj.getAllOrders();
//			System.out.println(orders.get(0));
		}
		catch (Exception e) {
			System.out.println("Error in the query");
		}

		


	}

}
