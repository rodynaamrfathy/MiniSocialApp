package models;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import enums.RoleEnum;

/**
 * 🧑‍💼 User Entity – Relationships Summary 🍙
 * 
 * [User] -------------------------< [GroupMembership]       (1 : N)
 * [User] -------------------------< [GroupPost]             (1 : N)
 * [User] -------------------------< [UserPost]              (1 : N)
 * [User] -------------------------< [Comment]               (1 : N)
 * [User] -------------------------< [Like]                  (1 : N)
 * [User] ------------------------<> [Group] (as Admin)      (M : N)
 * [User] -------------------------< [Friendships]           (1 : N)
 * [User] ----- (sent) ------------< [FriendshipRequests]    (1 : N)
 * [User] ----- (received) --------< [FriendshipRequests]    (1 : N)
 *
 * Legend:
 *   -->  One-to-Many (1:N)
 *   <--> Many-to-Many (M:N)
 */

@Entity
public class User {

    // 🔑 Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 👑 Admins for groups (Many-to-Many)
    @ManyToMany
    @JoinTable(
        name = "group_admins", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> adminOfGroups = new HashSet<>();
    
    // 📋 User Info
    @Column(length = 50)
    private String firstName;



	public Set<Group> getAdminOfGroups() {
		return adminOfGroups;
	}



	public void setAdminOfGroups(Set<Group> adminOfGroups) {
		this.adminOfGroups = adminOfGroups;
	}



	public Set<Group> getAdminOfGroups1() {
		return adminOfGroups1;
	}
	
	

	public void setAdminOfGroups1(Set<Group> adminOfGroups1) {
		this.adminOfGroups1 = adminOfGroups1;
	}

	@Column(length = 50)
    private String lastName;

    @Column(length = 50)
    private String userName;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Column(length = 50)
    private String password;

    @Column(length = 50)
    private String bio;

    // 🛡️ User Role (Admin / User) — Enum Stored as String
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Temporal(TemporalType.DATE)
    private Date birthdate;

    // 🔗 1️⃣ User ↔ Memberships (Many Groups)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<GroupMembership> memberships;

    // 👑 2️⃣ User ↔ Admin of Groups (Many Admins per Group)
    @ManyToMany(mappedBy = "groupAdmins", fetch = FetchType.LAZY)
    private Set<Group> adminOfGroups1;

    // ❤️ 3️⃣ User ↔ Likes (A user can like many posts)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Like> listOfLikedPosts;

    // 💬 4️⃣ User ↔ Comments (User can create many comments)
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private Set<Comment> commentsList;

    // 🏘️ 5️⃣ User ↔ GroupPosts (User posts in groups)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<GroupPost> groupPosts;

    // 🏠 6️⃣ User ↔ UserPosts (User posts on own timeline)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<UserPost> userPosts;

    // 👫 7️⃣ User ↔ Friendships (Confirmed friends)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Friendships> friendships;

    // 📨 8️⃣ Sent Friend Requests (User is requester)
    @OneToMany(mappedBy = "requester", fetch = FetchType.LAZY)
    private Set<FriendshipRequests> sentRequests;

    // 📥 9️⃣ Received Friend Requests (User is receiver)
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private Set<FriendshipRequests> receivedRequests;

    // ✅ All Getters & Setters...

    // Ensure no circular references in toString()
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

    // Getters and setters (same as your original ones)

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

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Set<GroupMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<GroupMembership> memberships) {
        this.memberships = memberships;
    }


    public Set<Like> getListOfLikedPosts() {
        return listOfLikedPosts;
    }

    public void setListOfLikedPosts(Set<Like> listOfLikedPosts) {
        this.listOfLikedPosts = listOfLikedPosts;
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
}
