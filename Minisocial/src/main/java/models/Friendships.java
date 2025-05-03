package models;

import javax.persistence.*;
import java.util.Date; 

/**
 * 🤝 Friendships Entity – Relationships Summary 🧂
 * 
 * Represents a confirmed bidirectional friendship:
 * 
 * [User] ---1---------------------< [Friendships] >---------------------1--- [User]
 *       (as user)                                           (as friend)
 * 
 * Each row is a single record of a friendship between two users.
 */

@Entity
public class Friendships {

    // 🔑 Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int friendship_id;

    // 👤 User who added the friend
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 👥 The friend that was added
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    // 📆 Since when they are friends
    @Temporal(TemporalType.DATE)
    private Date since;

    // ✅ Constructor
    public Friendships() {}

    public int getFriendshipId() {
        return friendship_id;
    }

    public int getFriendship_id() {
        return friendship_id;
    }

    public void setFriendship_id(int friendship_id) {
        this.friendship_id = friendship_id;
    }

    public void setFriendshipId(int friendship_id) {
        this.friendship_id = friendship_id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public User getFriend() {
        return friend;
    }
    
    public void setFriend(User friend) {
        this.friend = friend;
    }
    
    public Date getSince() {
        return since;
    }

    public void setSince(Date since) {
        this.since = since;
    }

    // 📝 toString() method for a readable string representation of the Friendships entity
    @Override
    public String toString() {
        return "Friendships{" +
               "friendship_id=" + friendship_id +
               ", user=" + (user != null ? user.getUserName() : "null") +
               ", friend=" + (friend != null ? friend.getUserName() : "null") +
               ", since=" + since +
               '}';
    }
}
