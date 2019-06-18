package com.academy.onlineAcademy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;

public class CourseController {
	
    private static final EntityManagerFactory emFactoryObj;
    private static final String PERSISTENCE_UNIT_NAME = "personPersistence";  
 
    static {
        emFactoryObj = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
    
    public static EntityManager getEntityManager() {
        return emFactoryObj.createEntityManager();
    }
	
	//EntityManager em;
	EntityManager emUPD;
	EntityManager emDEL;
	
	public CourseController() {
	  //em = emFactoryObj.createEntityManager();
	  emUPD = emFactoryObj.createEntityManager();
	  emDEL = emFactoryObj.createEntityManager();
	}
	
	public void addCourse(String name, String description, String teacherName, int duration, Level level,
			Category category, double price, boolean givesCertificate, byte[] coverPhoto) {
		EntityManager em = null;
		try {
			em = emFactoryObj.createEntityManager();
            
		    em.getTransaction().begin();
//		    Query query = em.createNativeQuery("INSERT INTO TCourse (name, description, teacher_name, duration, level, category, price, "
//					+ "gives_certificate, cover_photo) VALUES (?,?,?,?,?,?,?,?,?)");
//		        query.setParameter(1, name);
//		        query.setParameter(2, description);
//		        query.setParameter(3, teacherName);
//		        query.setParameter(4, duration);
//		        query.setParameter(5, level.name());
//		        query.setParameter(6, category.name());
//		        query.setParameter(7, price);
//		        query.setParameter(8, givesCertificate);
//		        query.setParameter(9, coverPhoto);
//		        query.executeUpdate();
		        
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
		try {

		    em.getTransaction().begin();
		    
			TypedQuery<Course> query = em.createNamedQuery("getAllCourses", Course.class);
		    em.getTransaction().commit();
		    return query.getResultList();
		} 
		catch(PersistenceException e) {

		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		em.close();
		}
	}
	
	public Course getCourseByName(String name) {
		EntityManager em = emFactoryObj.createEntityManager();
		try {

		    em.getTransaction().begin();
		    
		    TypedQuery<Course> query = em.createNamedQuery("findCourseByName", Course.class);
		    em.getTransaction().commit();
		    return query.setParameter("name", name).getSingleResult();
		} 
		catch(PersistenceException e) {

			e.printStackTrace();
		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		em.close();
		}
	}
	
	public int updateCourseById(int id) {
		try {

		    emUPD.getTransaction().begin();
		    
		    int updateCount = emUPD.createNamedQuery("Course.updateCourseById", Course.class).executeUpdate();
		    
		    //emUPD.merge(entity);
		    
		    emUPD.getTransaction().commit();
		    return updateCount;
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
		try {

		    emDEL.getTransaction().begin();
		    
		    int count = emDEL.createNamedQuery("Course.deleteAllCourses", Course.class).executeUpdate();
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
	
	public int deleteCourseById(int id) {
		try {

		    emDEL.getTransaction().begin();
		    
		    int count = emDEL.createNamedQuery("Course.deleteByCourseId", Course.class).executeUpdate();
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
	
	
}
