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
	
	EntityManager emUPD;
	EntityManager emDEL;
	
	public CourseController() {
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
		EntityManager em = null;
		try {
            em = emFactoryObj.createEntityManager();
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
		EntityManager em = null;
		try {
			em = emFactoryObj.createEntityManager();
		    em.getTransaction().begin();
		    
		    TypedQuery<Course> query = em.createNamedQuery("findCourseByName", Course.class);
		    em.getTransaction().commit();
		    return query.setParameter("name", name).getSingleResult();
		} 
		catch(PersistenceException e) {
			
		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		em.close();
		}
	}
	
	public List<Order> getAllCoursesByUser(int userId) {
		EntityManager em = null;
		try {
			em = emFactoryObj.createEntityManager();
		    em.getTransaction().begin();
		    
			TypedQuery<Order> query = em.createNamedQuery("", Order.class);
		    em.getTransaction().commit();
		    return query.setParameter("user_id", userId).getResultList();
		} catch(PersistenceException e) {

		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		em.close();
		}
	}
	
	public void updateCourseById(Course course, int id, String name, String description, String teacherName, int duration, Level level,
			Category category, double price, boolean givesCertificate, byte[] coverPhoto) {
		try {

		    emUPD.getTransaction().begin();
		    
		    Course newCourse = emUPD.merge(course);
		    newCourse.setName(name);
		    newCourse.setDescription(description);
		    newCourse.setTeacherName(teacherName);
		    newCourse.setDuration(duration);
		    newCourse.setLevel(level);
		    newCourse.setCategoty(category);
		    newCourse.setPrice(price);
		    newCourse.setGivesCertificate(givesCertificate);
		    newCourse.setCoverPhoto(coverPhoto);
		    
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
		try 
		{

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
		
		EntityManager emDEL = emFactoryObj.createEntityManager();
		try {

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
