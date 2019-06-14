package com.academy.onlineAcademy.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.academy.onlineAcademy.model.Person;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Type;

public class JPA {
	
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
 
        //List<Course> johnList = new ArrayList<Course>();
        //List<Course> samList = new ArrayList<Course>();
        List<Course> emmaList = new ArrayList<Course>();
//        
        //Person person1 = new Person(1, "John Smith", "johnny", "pass", "email", null, Type.ADMIN, johnList);
        //Person person2 = new Person(2, "Sam Pederson", "sam", "sth@mail.com", "pass", null, Type.ADMIN, samList);
        Person person3 = new Person(3, "Emma Stevenson", "emma", "st5h@mail.com", "pass", null, Type.USER, emmaList, null);
//        
        //entityMgr.persist(person1);
        //entityMgr.persist(person2);
        entityMgr.persist(person3);
// 
        
        entityMgr.getTransaction().commit();
 
        entityMgr.clear();
        System.out.println("Record Successfully Inserted In The Database");
		
//		  Person person1 = entityMgr.find(Person.class, 1);
//		  Person person2 = entityMgr.find(Person.class, 2);
//		  Person person3 = entityMgr.find(Person.class, 3);
//

//		  entityMgr.remove(person1);
		  //entityMgr.remove(person2);
//		  if (person2 != null) {
//			  entityMgr.remove(person2);
//			}
//		  entityMgr.remove(person3);
//		  entityMgr.getTransaction().commit();

	}

}
