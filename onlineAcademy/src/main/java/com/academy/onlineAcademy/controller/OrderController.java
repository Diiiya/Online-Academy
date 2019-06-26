package com.academy.onlineAcademy.controller;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.academy.onlineAcademy.model.Order;
import com.academy.onlineAcademy.model.Person;

public class OrderController {
	
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
	
	public OrderController() {
		
		// em = emFactoryObj.createEntityManager();
		 emUPD = emFactoryObj.createEntityManager();
		 emDEL = emFactoryObj.createEntityManager();
		
	}

	public void addOrder(int userId, int courseId, Date purchaseDate, boolean isPaid, double price) {
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
		    
		    Order order = new Order(userId, courseId, purchaseDate, isPaid, price);
		    em.persist(order);
		    
		    em.getTransaction().commit();
		} catch(PersistenceException e) {

		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		em.close();
		}
	}
	
	public List<Order> getAllOrders() {
		EntityManager em = null;
		try {
			em = emFactoryObj.createEntityManager();
		    em.getTransaction().begin();
		    
			TypedQuery<Order> query = em.createNamedQuery("getAllOrders", Order.class);
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
	
	public List<Order> getAllOrdersByUser(int userId) {
		EntityManager em = null;
		try {
			em = emFactoryObj.createEntityManager();
		    em.getTransaction().begin();
		    
			TypedQuery<Order> query = em.createNamedQuery("getAllOrdersByUser", Order.class);
		    em.getTransaction().commit();
		    return query.setParameter("userId", userId).getResultList();
		} catch(PersistenceException e) {

		    em.getTransaction().rollback();
		    
		    throw e;
		}
        finally {
		em.close();
		}
	}
	
	public Order getOrderById(int id) {
		EntityManager em = null;
		try {
			em = emFactoryObj.createEntityManager();
		    em.getTransaction().begin();
		    
		    TypedQuery<Order> query = em.createNamedQuery("findOrderById", Order.class);
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
	
	public int deleteOrderById(int id) {
		try {

		    emDEL.getTransaction().begin();
		    
		    int count = emDEL.createNamedQuery("deleteByOrderId", Person.class).executeUpdate();
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
