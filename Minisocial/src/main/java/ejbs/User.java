package ejbs;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Stateless
@Entity
public class User {

	public enum Role { user , admin};
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	
	@Column(name= "first_name", length = 50)
	private String firstName;
	
	@Column(name= "last_name", length = 50)
	private String lastName;
	
	@Column(name= "user_name", length= 50, unique = true)
	private String userName;
	
	@Column(length = 50, unique = true)
	private String email;
	
	@Column(length = 50)
	private String password;
	
	@Column(length = 50)
	private String bio;
	
	@Temporal(TemporalType.DATE)
	private Date birthdate;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "ENUM('user', 'admin')")
	private Role role;
}
