package service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import models.User;

@Stateless
public class UserService {

	@PersistenceContext(unitName = "hello")
	private EntityManager em;
	
	public void createUser(User user) {
		em.persist(user);
	}
	
	public User getUserById(int id) {
		return em.find(User.class,id);
	}
	
	public List<User> getAllUsers() {
		return em.createQuery("SELECT u from User u",User.class).getResultList();
	}
}
