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

public class OrderController {
	
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
	
	public OrderController() {
		
		 em = emf.createEntityManager();
		 emUPD = emf.createEntityManager();
		 emDEL = emf.createEntityManager();
		
	}

	public void addOrder(EntityManager em, int id, int userId, int courseId, Date purchaseDate, boolean isPaid, double price) {
		try {

		    em.getTransaction().begin();
		    // Do something with the EntityManager such as persist(), merge() or remove()
		    Query query = em.createNativeQuery("INSERT INTO Order (id, user_id, course_id, is_paid, price) VALUES (?,?,?,?,?)");
		        query.setParameter(1, id);
		        query.setParameter(2, userId);
		        query.setParameter(3, courseId);
		        query.setParameter(4, purchaseDate);
		        query.setParameter(5, isPaid);
		        query.setParameter(6, price);
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
	
	public List<Order> getAllOrdersByUser(EntityManager em, int userId) {
		try {

		    em.getTransaction().begin();
		    // Do something with the EntityManager such as persist(), merge() or remove()
			TypedQuery<Order> query = em.createNamedQuery("SELECT o FROM Order o WHERE o.userId = :userId", Order.class);
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
	
	public Order getOrderById(EntityManager em, int id) {
		try {

		    em.getTransaction().begin();
		    // Do something with the EntityManager such as persist(), merge() or remove()
		    TypedQuery<Order> query = em.createNamedQuery("SELECT o FROM Order o WHERE o.id = :id", Order.class);
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
	
}
