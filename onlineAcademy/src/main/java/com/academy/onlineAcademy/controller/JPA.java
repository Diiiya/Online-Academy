package com.academy.onlineAcademy.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.academy.onlineAcademy.model.Person;
import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.academy.onlineAcademy.model.Type;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;

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
//        EntityManager entityMgr = getEntityManager();
//        entityMgr.getTransaction().begin();
 
        //List<Course> johnList = new ArrayList<Course>();
        //List<Course> samList = new ArrayList<Course>();
        //List<Course> emmaList = new ArrayList<Course>();
//        
        //Person person1 = new Person(1, "John Smith", "johnny", "pass", "email", null, Type.ADMIN, johnList);
        //Person person2 = new Person(2, "Sam Pederson", "sam", "sth@mail.com", "pass", null, Type.ADMIN, samList);
//        Person person3 = new Person(3, "Emma Stevenson", "emma", "st5h@mail.com", "pass", null, Type.USER, emmaList, null);
//        
        
        //entityMgr.persist(person1);
        //entityMgr.persist(person2);
//        entityMgr.persist(person3);
//// 
//        
//        entityMgr.getTransaction().commit();
//// 
//        entityMgr.clear();
//        System.out.println("Record Successfully Inserted In The Database");
		
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
        
        

        // 2nd
//        EntityManager entityMgr = getEntityManager();
//        entityMgr.getTransaction().begin();
//        Course course = new Course("MOVIE 2", "Descr", "SMITH", 5, Level.ADVANCED, Category.ARTS, 200, true, null);
//        entityMgr.persist(course);
//        entityMgr.getTransaction().commit();
//        entityMgr.clear();
//        System.out.println("Record Successfully Inserted In The Database");
        
        
        // 3rd
        
        CourseController obj = new CourseController();
        CourseController obj2 = new CourseController();
		//getClass().getResourceAsStream("")

		
		try {
			//Category category = Category.valueOf(Category.class, "ARTS");
			//Course artCourse = new Course("MOVIE 3", "Descr", "SMITH", 5, Level.ADVANCED, Category.ARTS, 200, true, null);
			obj.addCourse("MOVIE 5", "Descr", "SMITH", 5, Level.ADVANCED, Category.ARTS, 200, true, null);
			Course artCourse = obj2.getCourseByName("MOVIE 5");
			artCourse.getCategory();
			System.out.println(artCourse.getCategory());
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
        }


}
