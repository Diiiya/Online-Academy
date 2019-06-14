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
	
	EntityManagerFactory emf;
	EntityManager em;
	EntityManager emUPD;
	EntityManager emDEL;
	
	public CourseController() {
	  
	  em = emf.createEntityManager();
	  emUPD = emf.createEntityManager();
	  emDEL = emf.createEntityManager();
//	  em.getMetamodel().managedType(Course.class);
//	  emUPD.getMetamodel().managedType(Course.class);
//	  emDEL.getMetamodel().managedType(Course.class);
	}
	
	public void addCourse(EntityManager em, int id, String name, String description, String teacherName, int duration, Level level,
			Category category, double price, boolean givesCertificate, byte[] coverPhoto) {
		try {

		    em.getTransaction().begin();
		    // Do something with the EntityManager such as persist(), merge() or remove()
		    Query query = em.createNativeQuery("INSERT INTO Course (id, name, description, teacher_name, duration, level, category, price, "
					+ "gives_certificate, cover_photo) VALUES (?,?,?,?,?,?,?,?,?,?)");
		        query.setParameter(1, id);
		        query.setParameter(2, name);
		        query.setParameter(3, description);
		        query.setParameter(4, teacherName);
		        query.setParameter(5, duration);
		        query.setParameter(6, level);
		        query.setParameter(7, category);
		        query.setParameter(8, price);
		        query.setParameter(9, givesCertificate);
		        query.setParameter(10, coverPhoto);
		        query.executeUpdate();
		    em.getTransaction().commit();
		} catch(PersistenceException e) {

		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		em.close();
		}
	}
	
	public List<Course> getAllCourses(EntityManager em) {
		try {

		    em.getTransaction().begin();
		    // Do something with the EntityManager such as persist(), merge() or remove()
			TypedQuery<Course> query = em.createNamedQuery("Course.getAllCourses", Course.class);
//			allCoursesList = query.getResultList();
//			return allCoursesList;
		    em.getTransaction().commit();
		    return query.getResultList();
		} catch(PersistenceException e) {

		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		em.close();
		}
	}
	
	public Course getCourseByName(EntityManager em, String name) {
		try {

		    em.getTransaction().begin();
		    // Do something with the EntityManager such as persist(), merge() or remove()
		    TypedQuery<Course> query = em.createNamedQuery("Course.findCourseByName", Course.class);
		    em.getTransaction().commit();
		    return query.setParameter("name", name).getSingleResult();
		} catch(PersistenceException e) {

		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		em.close();
		}
	}
	
	public int updateCourseById(EntityManager emUPD, int id) {
		try {

		    em.getTransaction().begin();
		    // Do something with the EntityManager such as persist(), merge() or remove()
		    int updateCount = emUPD.createNamedQuery("Course.updateCourseById", Course.class).executeUpdate();
		    em.getTransaction().commit();
		    return updateCount;
		} catch(PersistenceException e) {

		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		em.close();
		}
	}

	public int deleteAllCourse(EntityManager emDEL, int id) {
		try {

		    em.getTransaction().begin();
		    // Do something with the EntityManager such as persist(), merge() or remove()
		    int count = emDEL.createNamedQuery("Course.deleteAllCourses", Course.class).executeUpdate();
		    em.getTransaction().commit();
		    return count;
		} catch(PersistenceException e) {

		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		em.close();
		}
	}
	
	public int deleteCourseById(EntityManager emDEL, int id) {
		try {

		    em.getTransaction().begin();
		    // Do something with the EntityManager such as persist(), merge() or remove()
		    int count = emDEL.createNamedQuery("Course.deleteByCourseId", Course.class).executeUpdate();
		    em.getTransaction().commit();
		    return count;
		} catch(PersistenceException e) {

		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		em.close();
		}
	}
	
	
}
