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
        EntityManager entityMgr = getEntityManager();
        entityMgr.getTransaction().begin();
 
//        
        Date date = new Date();
        Order order = new Order(1, 1, date, true, 50);
        entityMgr.persist(order);
// 
        
        entityMgr.getTransaction().commit();
 
        entityMgr.clear();
        System.out.println("Record Successfully Inserted In The Database");
		


	}

}
