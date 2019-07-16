package com.academy.onlineAcademy.controller;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.academy.onlineAcademy.model.Course;
import com.academy.onlineAcademy.model.Order;
import com.academy.onlineAcademy.model.Person;

public class OrderController {
	
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
	public OrderController() {
		
	}

//	public void addOrder(int userId, int courseId, Date purchaseDate, boolean isPaid, double price) {
	/**
	 * Method that adds a new order to the database
	 * @param order - the order object 
	 */
	public void addOrder(Order order) {
		EntityManager em = null;
		try {
			em = emFactoryObj.createEntityManager();
		    em.getTransaction().begin();
//		    Query query = em.createNativeQuery("INSERT INTO Order (user_id, course_id, is_paid, price) VALUES (?,?,?,?,?)");
//		        query.setParameter(1, userId);
//		        query.setParameter(2, courseId);
//		        query.setParameter(3, purchaseDate);
//		        query.setParameter(4, isPaid);
//		        query.setParameter(5, price);
//		        query.executeUpdate();
		    
		    //Order order = new Order(userId, courseId, purchaseDate, isPaid, price);
		    em.persist(order);
		    
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
	 * Method that gets all the orders from the database
	 * @return List<Order> with all the retrieved orders
	 */
	public List<Order> getAllOrders() {
		EntityManager em = emFactoryObj.createEntityManager();
		TypedQuery<Order> query = em.createNamedQuery("getAllOrders", Order.class);
	    return query.getResultList();
	}
	
	/**
	 * Method that gets all the orders for a specific user that are placed but not paid (not completed) from the database
	 * @param userId - the unique id of the specific user
	 * @return List<Order> list of the retrieved unpaid orders for the selected user
	 */
	public List<Order> getAllUnpaidOrdersByUser(int userId) {
		EntityManager em = emFactoryObj.createEntityManager();
		TypedQuery<Order> query = em.createNamedQuery("getAllUnpaidOrdersByUser", Order.class);
	    return query.setParameter("userId", userId).getResultList();
	}
	
	/**
	 * Method that gets all the orders for a specific user that are placed and paid (completed) from the database
	 * @param userId - the unique id of the specific user
	 * @return List<Order> list of the retrieved paid orders for the selected user
	 */
	public List<Order> getAllPaidOrdersByUser(int userId) {
		EntityManager em = emFactoryObj.createEntityManager();
		TypedQuery<Order> query = em.createNamedQuery("getAllPaidOrdersByUser", Order.class);
	    return query.setParameter("userId", userId).getResultList();
	}
	
	/**
	 * Methods that gets a specific order from the database based on its id from the database
	 * @param id - the unique id of the order
	 * @return Order object with the specified id
	 */
	public Order getOrderById(int id) {
		EntityManager em = emFactoryObj.createEntityManager();
	    TypedQuery<Order> query = em.createNamedQuery("findOrderById", Order.class);
	    return query.setParameter("id", id).getSingleResult();
	}
	
	/**
	 * Method that updates a specific order object from the database
	 * @param order - the order that has to be updated
	 */
	public void updateOrder(Order order) {
		EntityManager emUPD = null;
		try {
			emUPD = emFactoryObj.createEntityManager();
		    emUPD.getTransaction().begin();
		    
		    Order newOrder = emUPD.merge(order);
		    
		    emUPD.persist(newOrder);
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
	 * Method that deletes an order from the database
	 * @param id - the unique id of the order
	 * @return the count of the deleted orders (should always be one, as we delete order by unique id)
	 */
	public int deleteOrderById(int id) {
		EntityManager emDEL = null;
		try {
			emDEL = emFactoryObj.createEntityManager();
		    emDEL.getTransaction().begin();
		    
		    int count = emDEL.createNamedQuery("deleteByOrderId", Order.class).setParameter("id", id).executeUpdate();
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
