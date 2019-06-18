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

	public void addPerson(String fullName, String username, String email, String password, byte[] photo, Type type,
			List<Course> listOfCourses, List<Order> listOfOrders) {
		try {

		    em.getTransaction().begin();
		    
//		    Query query = em.createNativeQuery("INSERT INTO Course (id, full_name, username, email, password, photo, type, ccourses, corders) VALUES (?,?,?,?,?,?,?,?,?)");
//		        query.setParameter(1, fullName);
//		        query.setParameter(2, username);
//		        query.setParameter(3, email);
//		        query.setParameter(4, password);
//		        query.setParameter(5, photo);
//		        query.setParameter(6, type);
//		        query.setParameter(7, listOfCourses);
//		        query.setParameter(8, listOfOrders);
//		        query.executeUpdate();
		    
		    Person person = new Person(fullName, username, email,password, photo, type, listOfCourses, listOfOrders);
		    em.persist(person);
		    
		    em.getTransaction().commit();
		} catch(PersistenceException e) {

		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		em.close();
		}
	}
	
	public List<Person> getAllUsers() {
		try {

		    em.getTransaction().begin();
		    
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
	
	public Person getPersonById(int id) {
		try {

		    em.getTransaction().begin();
		    
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
	
	public Person getPersonByName(String name) {
		try {

		    em.getTransaction().begin();
		    
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
	
	public int updatePersonById(int id) {
		try {

		    emUPD.getTransaction().begin();
		    
		    int updateCount = emUPD.createNamedQuery("Person.updatePersonById", Person.class).executeUpdate();
		    emUPD.getTransaction().commit();
		    return updateCount;
		} catch(PersistenceException e) {

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
		    
		    int count = emDEL.createNamedQuery("Person.deleteAllUsers", Person.class).executeUpdate();
		    emDEL.getTransaction().commit();
		    return count;
		} catch(PersistenceException e) {

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
		    
		    int count = emDEL.createNamedQuery("Person.deleteByPersonId", Person.class).executeUpdate();
		    emDEL.getTransaction().commit();
		    return count;
		} catch(PersistenceException e) {

		    emDEL.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		emDEL.close();
		}
	}
	
}
