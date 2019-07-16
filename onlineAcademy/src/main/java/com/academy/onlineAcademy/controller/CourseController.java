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
    
    /**
     * Creates the Entity Manager object.
     * @return EntityManager
     */
    public static EntityManager getEntityManager() {
        return emFactoryObj.createEntityManager();
    }
	
	/**
	 * Class constructor.
	 */
	public CourseController() {
	  
	}
	
	/**
	 * Method that add a new course to the database with given parameters
	 * @param name - course name
	 * @param description - course description
	 * @param teacherName - name of the teacher of the course
	 * @param duration - duration of the course
	 * @param level - level of difficulty of the course
	 * @param category - category to which the course belongs to
	 * @param price - price for the course
	 * @param givesCertificate - whether or not the course awards a certificate upon completion
	 * @param coverPhoto - image for the course page
	 */
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
	
	/**
	 * Method that gets/reads all courses from the database
	 * @return List<Course> - all the found courses
	 */
	public List<Course> getAllCourses() {
		EntityManager em = emFactoryObj.createEntityManager();
		TypedQuery<Course> query = em.createNamedQuery("getAllCourses", Course.class);
	    return query.getResultList();
	}
	
	/**
	 * Method that gets a course from the database by the given name
	 * @param name - unique name of the course
	 * @return Course object - the retrieved course from the database, matching the given name
	 */
	public Course getCourseByName(String name) {
		EntityManager em = emFactoryObj.createEntityManager();		    
	    TypedQuery<Course> query = em.createNamedQuery("findCourseByName", Course.class);
	    
	    return query.setParameter("name", name).getSingleResult();
	}
	
	/**
	 * Method that gets a course from the database by the given id
	 * @param id - unique id of the course
	 * @return Course object - the retrieved course from the database, matching the given id
	 */
	public Course getCourseById(int id) {
		EntityManager em = emFactoryObj.createEntityManager();
	    TypedQuery<Course> query = em.createNamedQuery("findCourseById", Course.class);
	    
	    return query.setParameter("id", id).getSingleResult();
	}
	
	/**
	 * Method with 1 parameter that returns a list of courses for a specific user depending on the entered user id
	 * @param userId - unique id of the user
	 * @return List<Courses> - the retrieved courses for the specific user
	 */
	public List<Order> getAllCoursesByUser(int userId) {
		EntityManager em = emFactoryObj.createEntityManager();
		TypedQuery<Order> query = em.createNamedQuery("", Order.class);
		
	    return query.setParameter("user_id", userId).getResultList();
	}
	
	/**
	 * Method that updates a course in the database based on a given course object
	 * @param course - the course object that has to be updated
	 */
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

	/**
	 * Method that deletes all the courses from the database
	 * @return int - the count of the deleted courses
	 */
	public int deleteAllCourses() {
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
	
	/**
	 * Method with 1 parameter that deletes a specific course
	 * @param id - the id of the course that has to be deleted
	 */
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
