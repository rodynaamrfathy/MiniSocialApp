package service;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.*;
import models.User;

@Stateless
public class UserService {

	@PersistenceContext(unitName = "hello")
	private EntityManager em;
	
	public User login(String userName, String password) {
        TypedQuery<User> query = em.createQuery(
            "SELECT u FROM User u WHERE u.userName = :userName AND u.password = :password", User.class);
        query.setParameter("userName", userName);
        query.setParameter("password", password); 
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
