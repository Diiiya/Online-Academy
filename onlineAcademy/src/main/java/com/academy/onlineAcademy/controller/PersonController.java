package com.academy.onlineAcademy.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.academy.onlineAcademy.exceptions.UpdateUserException;
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
    
    /**
     * Creates the Entity Manager object.
     * @return EntityManager
     */
    public static EntityManager getEntityManager() {
        return emFactoryObj.createEntityManager();
    }
	
    /**
     * Class constructor
     */
	public PersonController() {
		  
		}

	/**
	 * Methods that adds a new user to the database
	 * @param fullName - full name of the user
	 * @param username - unique user name
	 * @param email - unique email of the user
	 * @param password 
	 * @param photo - optional profile photo of the user
	 * @param type
	 * @param listOfCourses - null by default, should hold the paid orders/courses of the user
	 * @param listOfOrders - null by default, should hold the unpaid orders of the user
	 */
	public void addPerson(String fullName, String username, String email, String password, byte[] photo, Type type,
			List<Course> listOfCourses, List<Order> listOfOrders) {
		    EntityManager em = null;
		try {
			em = emFactoryObj.createEntityManager();

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
		    
		    Person person = new Person(fullName, username, email, password, photo, type, listOfCourses, listOfOrders);
		    em.persist(person);
		    
		    em.getTransaction().commit();
		} 
		catch(PersistenceException e) {

		    em.getTransaction().rollback();
		    throw e;
		}
        finally {
		    em.close();
		}
	}
	
	/**
	 * Method that gets all the users from the database
	 * @return List<Person> with all the retrieved results
	 */
	public List<Person> getAllUsers() {
		EntityManager em = emFactoryObj.createEntityManager();
		TypedQuery<Person> query = em.createNamedQuery("getAllUsers", Person.class);
	    return query.getResultList();
	}
	
	/**
	 * Method that gets a person by a specific id from the database
	 * @param id - unique id of the user
	 * @return Person object - the retrieved person
	 */
	public Person getPersonById(int id) {
		EntityManager em = emFactoryObj.createEntityManager();	    
	    TypedQuery<Person> query = em.createNamedQuery("findUserById", Person.class);
	    return query.setParameter("id", id).getSingleResult();
	}
	
	/**
	 * Method that gets a person by a specific name from the database
	 * @param name - the full name of the user
	 * @return Person object - the retrieved person
	 */
	public Person getPersonByName(String name) {
		EntityManager em = emFactoryObj.createEntityManager();
		TypedQuery<Person> query = em.createNamedQuery("findUserByName", Person.class);
		return query.setParameter("name", name).getSingleResult();
	}
	
	/**
	 * Method that gets a person by a specific user name from the database
	 * @param username - unique user name 
	 * @return Person object - the retrieved person
	 */
	public Person getPersonByUsername(String username) {
		EntityManager em = emFactoryObj.createEntityManager();		    
	    TypedQuery<Person> query = em.createNamedQuery("findUserByUsername", Person.class);
	    return query.setParameter("username", username).getSingleResult();
	}
	
	/**
	 * Method that gets a person by a specific email from the database
	 * @param email - unique user email
	 * @return Person object - the retrieved person
	 */
	public Person getPersonByEmail(String email) {
		EntityManager em = emFactoryObj.createEntityManager();	    
	    TypedQuery<Person> query = em.createNamedQuery("findUserByEmail", Person.class);
	    return query.setParameter("email", email).getSingleResult();
	}
	
//	public void updatePersonById(Person person, int id, String fullName, String username, String email, String password, Type type) {
//		try {
//
//		    emUPD.getTransaction().begin();
//		    
//		    Person updatedPerson = emUPD.merge(person);
//		    
//		    updatedPerson.setFullName(fullName);
//		    updatedPerson.setUsername(username);
//		    updatedPerson.setEmail(email);
//		    updatedPerson.setPassword(password);
//		    updatedPerson.setType(type);
//		    
//		    emUPD.persist(updatedPerson);
//		    emUPD.getTransaction().commit();
//		    
//		} catch(PersistenceException e) {
//
//		    emUPD.getTransaction().rollback();
//		    
//		    throw e;
//		}
//        finally {
//		emUPD.close();
//		}
//	}
	
	/**
	 * Method that updates specific person in the database
	 * @param person - the specific person object to be updated
	 */
	public void updatePerson(Person person) {
		EntityManager emUPD = null;
		try {
			emUPD = emFactoryObj.createEntityManager();
		    emUPD.getTransaction().begin();
		    
		    Person updatedPerson = emUPD.merge(person);
		    
		    emUPD.persist(updatedPerson);
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
	 * Method that deletes all the users from the database
	 * @return int - the number of deleted users (person objects)
	 */
	public int deleteAllUsers() {
		EntityManager emDEL = null;
		try {
			emDEL = emFactoryObj.createEntityManager();
		    emDEL.getTransaction().begin();
		    
		    int count = emDEL.createNamedQuery("deleteAllUsers", Person.class).executeUpdate();
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
	 * Method that deletes a specific user from the database
	 * @param id - unique id of the user that has to be deleted
	 * @return
	 */
	public int deletePersonById(int id) {
		EntityManager emDEL = null;
		try {
			emDEL = emFactoryObj.createEntityManager();
		    emDEL.getTransaction().begin();
		    
		    int count = emDEL.createNamedQuery("deleteByPersonId", Person.class).setParameter("id", id).executeUpdate();
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
