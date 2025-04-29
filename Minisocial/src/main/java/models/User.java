package models;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

	
	
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
	
	@Column(length = 50)
	private String role;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}


	@Override
	public String toString() {
	    return "User{" +
	            "userId=" + userId +
	            ", firstName='" + firstName + '\'' +
	            ", lastName='" + lastName + '\'' +
	            ", userName='" + userName + '\'' +
	            ", email='" + email + '\'' +
	            ", bio='" + bio + '\'' +
	            ", role='" + role + '\'' +
	            '}';
	}





    

}
