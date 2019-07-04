package com.academy.onlineAcademy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.academy.onlineAcademy.model.Order;

public class CourseController {
	
    private static final EntityManagerFactory emFactoryObj;
    private static final String PERSISTENCE_UNIT_NAME = "personPersistence";  
 
    static {
        emFactoryObj = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
    
    public static EntityManager getEntityManager() {
        return emFactoryObj.createEntityManager();
    }
	
	
	public CourseController() {
	  
	}
	
	public void addCourse(String name, String description, String teacherName, int duration, Level level,
			Category category, double price, boolean givesCertificate, byte[] coverPhoto) {
		EntityManager em = null;
		try {
			em = emFactoryObj.createEntityManager();
            
		    em.getTransaction().begin();
		        
	        Course course = new Course(name, description, teacherName, duration, level, category, price, givesCertificate, coverPhoto);
		    em.persist(course);
		        
		    em.getTransaction().commit();
		    
		} 
		catch(PersistenceException e) {

		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
        	if (em != null) {
        		em.close();
        	}
		}
	}
	
	public List<Course> getAllCourses() {
		EntityManager em = emFactoryObj.createEntityManager();
		TypedQuery<Course> query = em.createNamedQuery("getAllCourses", Course.class);
	    return query.getResultList();
	}
	
	public Course getCourseByName(String name) {
		EntityManager em = emFactoryObj.createEntityManager();		    
	    TypedQuery<Course> query = em.createNamedQuery("findCourseByName", Course.class);
	    
	    return query.setParameter("name", name).getSingleResult();
	}
	
	public Course getCourseById(int id) {
		EntityManager em = emFactoryObj.createEntityManager();
	    TypedQuery<Course> query = em.createNamedQuery("findCourseById", Course.class);
	    
	    return query.setParameter("id", id).getSingleResult();
	}
	
	public List<Order> getAllCoursesByUser(int userId) {
		EntityManager em = emFactoryObj.createEntityManager();
		TypedQuery<Order> query = em.createNamedQuery("", Order.class);
		
	    return query.setParameter("user_id", userId).getResultList();
	}
	
	public void updateCourseById(Course course) {
		EntityManager emUPD = null;
		try {
			emUPD = emFactoryObj.createEntityManager();
		    emUPD.getTransaction().begin();
		    
		    Course newCourse = emUPD.merge(course);
		    
		    emUPD.persist(newCourse);
		    emUPD.getTransaction().commit();
		} 
		catch(PersistenceException e) {
		    emUPD.getTransaction().rollback();
		    throw e;
		}
        finally {
		    emUPD.close();
		}
	}

	public int deleteAllCourse(int id) {
		EntityManager emDEL = null;
		try 
		{
			emDEL = emFactoryObj.createEntityManager();
		    emDEL.getTransaction().begin();
		    int count = emDEL.createNamedQuery("deleteAllCourses", Course.class).executeUpdate();
		    emDEL.getTransaction().commit();
		    return count;
		} 
		catch(PersistenceException e) {
		    emDEL.getTransaction().rollback();
		    throw e;
		}
        finally {
		    emDEL.close();
		}
	}
	
	public void deleteCourseById(int id) {
		
		EntityManager emDEL = null;
		try {
			emDEL = emFactoryObj.createEntityManager();
		    emDEL.getTransaction().begin();
		    emDEL.createNamedQuery("deleteByCourseId", Course.class).setParameter("id", id).executeUpdate();
		    emDEL.getTransaction().commit();
		} 
		catch(PersistenceException e) {

			e.printStackTrace();
		    emDEL.getTransaction().rollback();
		    
		}
		finally {
        	emDEL.close();
		}
	}
	
	
}
