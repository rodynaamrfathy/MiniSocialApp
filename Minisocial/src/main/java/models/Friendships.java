package models;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents a confirmed friendship between two users.
 * 
 * This entity models a bidirectional friendship relationship:
 *  One user adds another as a friend.
 *  Tracks the friendship creation date.
 * 
 * Associations:
 *  Many friendships can be initiated by one user.
 *  Many friendships can refer to the same friend.
 */
@Entity
public class Friendships {

    /** Primary key: Unique ID of the friendship record. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int friendship_id;

    /** The user who added the friend. */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /** The friend that was added. */
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    /** The date when the friendship was established. */
    @Temporal(TemporalType.DATE)
    private Date since;

    /** Default constructor. */
    public Friendships() {}

    // === Getters and Setters ===

    public int getFriendshipId() {
        return friendship_id;
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

    /** Returns a string representation of the friendship record. */
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
