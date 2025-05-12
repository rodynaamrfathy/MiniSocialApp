package models;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents a 'Like' entity for a post or group post in the system.
 * This class models the user's interaction with posts, either as a UserPost or GroupPost.
 */
@Entity
@Table(name = "likes") // Table name set to plural to match naming conventions.
public class Like {

    // Unique identifier for the Like
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id") // Column name set to 'like_id' to follow naming conventions.
    private int likeId;

    // One-to-Many relationship with UserPost, nullable to allow for GroupPost usage
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true) // Nullable to accommodate both UserPost and GroupPost associations.
    private UserPost post;

    // One-to-Many relationship with GroupPost, nullable to allow for UserPost usage
    @ManyToOne
    @JoinColumn(name = "group_post_id", nullable = true) // Nullable to accommodate both GroupPost and UserPost associations.
    private GroupPost groupPost;

    // User who liked the post, a required relationship
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Non-nullable as each Like is associated with a User.
    private User user;

    // Timestamp to record when the Like was created
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date(); // Default to current timestamp for when the like is created.

    // ➡️ Getters and Setters

    /**
     * Retrieves the unique identifier for the Like.
     *
     * @return the Like ID.
     */
    public int getLikeId() {
        return likeId;
    }

    /**
     * Sets the unique identifier for the Like.
     *
     * @param likeId the Like ID to set.
     */
    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    /**
     * Retrieves the associated UserPost (if any) for this Like.
     *
     * @return the UserPost associated with the Like.
     */
    public UserPost getPost() {
        return post;
    }

    /**
     * Sets the associated UserPost for this Like.
     *
     * @param post the UserPost to associate with this Like.
     */
    public void setPost(UserPost post) {
        this.post = post;
    }

    /**
     * Retrieves the associated GroupPost (if any) for this Like.
     *
     * @return the GroupPost associated with the Like.
     */
    public GroupPost getGroupPost() {
        return groupPost;
    }

    /**
     * Sets the associated GroupPost for this Like.
     *
     * @param groupPost the GroupPost to associate with this Like.
     */
    public void setGroupPost(GroupPost groupPost) {
        this.groupPost = groupPost;
    }

    /**
     * Retrieves the user who liked the post.
     *
     * @return the User who liked the post.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who liked the post.
     *
     * @param user the User who liked the post.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Retrieves the timestamp when the Like was created.
     *
     * @return the timestamp of when the Like was created.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for when the Like was created.
     *
     * @param timestamp the timestamp to set.
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
