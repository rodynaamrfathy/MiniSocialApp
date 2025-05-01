package models;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import enums.RoleEnum;



@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
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
	
	@Enumerated(EnumType.STRING) 
	private RoleEnum role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<GroupMembership> memberships;
	
	@ManyToMany(mappedBy="groupAdmins")
	private Set<Group> adminOfGroups;

    @Temporal(TemporalType.DATE)
    private Date birthdate;
	
    @OneToMany(mappedBy = "user")
    private Set<Like> listOfLikedPosts;
	
    @OneToMany(mappedBy = "creator")
    private Set<Comment> commentsList;
	
	@OneToMany(mappedBy = "user")
	private Set<GroupPost> groupPosts;

	@OneToMany(mappedBy = "user")
	private Set<UserPost> userPosts;
	
	@OneToMany(mappedBy = "user")
    private Set<Friendships> friendships; // U1 <-> U2
	
	@OneToMany(mappedBy = "requester")
	private Set<FriendshipRequests> sentRequests;  // U1 -> U2

	@OneToMany(mappedBy = "receiver")
	private Set<FriendshipRequests> receivedRequests; // U2 <- U1
	
	public Set<Friendships> getFriendships() {
		return friendships;
	}

	public void setFriendships(Set<Friendships> friendships) {
		this.friendships = friendships;
	}

	public Set<FriendshipRequests> getSentRequests() {
		return sentRequests;
	}

	public void setSentRequests(Set<FriendshipRequests> sentRequests) {
		this.sentRequests = sentRequests;
	}

	public Set<FriendshipRequests> getReceivedRequests() {
		return receivedRequests;
	}

	public void setReceivedRequests(Set<FriendshipRequests> receivedRequests) {
		this.receivedRequests = receivedRequests;
	}

	public Long getUserId() {
    return userId;
	}

	public void setUserId(Long userId) {
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

	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}


	public Set<Comment> getCommentsList() {
		return commentsList;
	}

	public void setCommentsList(Set<Comment> commentsList) {
		this.commentsList = commentsList;
	}

	public Set<GroupPost> getGroupPosts() {
		return groupPosts;
	}

	public void setGroupPosts(Set<GroupPost> groupPosts) {
		this.groupPosts = groupPosts;
	}

	public Set<UserPost> getUserPosts() {
		return userPosts;
	}

	public void setUserPosts(Set<UserPost> userPosts) {
		this.userPosts = userPosts;
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

