package service;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import javax.persistence.*;
import models.User;

@Stateless
public class UserService {

	@PersistenceContext(unitName = "hello")
	private EntityManager em;
	
    //@Inject
	//private UserTransaction tx;

    
    // kont ha use UserTransaction begin, commit, rollback bs hya already aslun ejb
    public void manageProfile(User user) {
        User existingUserToUpdate = em.find(User.class, user.getUserId());

        if (existingUserToUpdate == null) {
        	throw new WebApplicationException("User not found", 404);
        }

        existingUserToUpdate.setFirstName(user.getFirstName());
        existingUserToUpdate.setLastName(user.getLastName());
        existingUserToUpdate.setEmail(user.getEmail());
        existingUserToUpdate.setBirthdate(user.getBirthdate());
        existingUserToUpdate.setBio(user.getBio());
        existingUserToUpdate.setPassword(user.getPassword());		
		
	}
	
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
