package com.academy.onlineAcademy.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Level;
import com.academy.onlineAcademy.model.Order;
import com.academy.onlineAcademy.model.Person;
import com.academy.onlineAcademy.model.Type;

public class PersonController {
	
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
	
	public PersonController() {
		  
		  em = emf.createEntityManager();
		  emUPD = emf.createEntityManager();
		  emDEL = emf.createEntityManager();
		}

	public void addPerson(EntityManager em, int id, String fullName, String username, String email, String password, byte[] photo, Type type,
			List<Course> listOfCourses, List<Order> listOfOrders) {
		try {

		    em.getTransaction().begin();
		    // Do something with the EntityManager such as persist(), merge() or remove()
		    Query query = em.createNativeQuery("INSERT INTO Course (id, full_name, username, email, password, photo, type, ccourses, corders VALUES (?,?,?,?,?,?,?,?,?)");
		        query.setParameter(1, id);
		        query.setParameter(2, fullName);
		        query.setParameter(3, username);
		        query.setParameter(4, email);
		        query.setParameter(5, password);
		        query.setParameter(6, photo);
		        query.setParameter(7, type);
		        query.setParameter(8, listOfCourses);
		        query.setParameter(9, listOfOrders);
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
	
	public List<Person> getAllUsers(EntityManager em) {
		try {

		    em.getTransaction().begin();
		    // Do something with the EntityManager such as persist(), merge() or remove()
			TypedQuery<Person> query = em.createNamedQuery("Person.getAllUsers", Person.class);
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
	
	public Person getPersonById(EntityManager em, int id) {
		try {

		    em.getTransaction().begin();
		    // Do something with the EntityManager such as persist(), merge() or remove()
		    TypedQuery<Person> query = em.createNamedQuery("Person.findUserById", Person.class);
		    em.getTransaction().commit();
		    return query.setParameter("id", id).getSingleResult();
		} catch(PersistenceException e) {

		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		em.close();
		}
	}
	
	public Person getPersonByName(EntityManager em, String name) {
		try {

		    em.getTransaction().begin();
		    // Do something with the EntityManager such as persist(), merge() or remove()
		    TypedQuery<Person> query = em.createNamedQuery("Person.findUserByName", Person.class);
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
	
	public int updatePersonById(EntityManager emUPD, int id) {
		try {

		    em.getTransaction().begin();
		    // Do something with the EntityManager such as persist(), merge() or remove()
		    int updateCount = emUPD.createNamedQuery("Person.updatePersonById", Person.class).executeUpdate();
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
		    int count = emDEL.createNamedQuery("Person.deleteAllUsers", Person.class).executeUpdate();
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
		    int count = emDEL.createNamedQuery("Person.deleteByPersonId", Person.class).executeUpdate();
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
