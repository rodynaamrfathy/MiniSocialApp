package models;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 🧾 UserPost – Relationships Summary 🧾
 * 
 * Subclass of Post:
 *   🔹 Inherits: comments, likes, content, imageUrl, publishDate
 * 
 * [UserPost] ---N-------------------> [User]      (N : 1)
 *                 (author)
 *
 * Legend:
 *   -->  Many-to-One (N:1)
 */

@Entity
public class UserPost extends Post {
	public UserPost() {
	    // Required for JSON-B and JPA
	}

    @Override
	public String toString() {
		return "UserPost [user=" + user + "]";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/** 👤 Author of the user post */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

 // In UserPost.java
    @Override
    public Group getGroup() {
        return null;  // No group for UserPost
    }
    @Override
    public String getType() {
        return "USER_POST"; // Return a string identifier specific to user posts
    }
}
