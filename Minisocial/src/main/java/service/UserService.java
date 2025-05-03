package service;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import models.User;

@Stateless
public class UserService {

	@PersistenceContext(unitName = "hello")
	private EntityManager em;
	

}
