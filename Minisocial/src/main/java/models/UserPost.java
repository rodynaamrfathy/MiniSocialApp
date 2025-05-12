package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * UserPost Entity
 * 
 * A subclass of Post representing a post authored by a user.
 * 
 * Inherits:
 *   - Comments
 *   - Likes
 *   - Content
 *   - Image URL
 *   - Publish Date
 * 
 * Relationships:
 *   - Many-to-One → {@link User}
 */
@Entity
public class UserPost extends Post {

    /** Default constructor required for JSON-B and JPA */
    public UserPost() {}

    /**
     * Returns a string representation of the UserPost.
     * 
     * @return Formatted string with the user reference.
     */
    @Override
    public String toString() {
        return "UserPost [user=" + user + "]";
    }

    /**
     * Gets the user who authored the post.
     * 
     * @return The {@link User} associated with this post.
     */
    @Override
    public User getUser() {
        return user;
    }

    /**
     * Sets the user (author) of the post.
     * 
     * @param user The {@link User} to associate with the post.
     */
    @Override
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Author reference: Many UserPosts can belong to one User.
     * 
     * Mapped via the foreign key column `user_id`.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Gets the group associated with this post.
     * 
     * Not applicable for UserPost. Always returns {@code null}.
     * 
     * @return {@code null} (no group associated).
     */
    @Override
    public Group getGroup() {
        return null;
    }

    /**
     * Gets the discriminator type of this post.
     * 
     * Used in the joined inheritance strategy.
     * 
     * @return A string representing the post type: "USER_POST"
     */
    @Override
    public String getType() {
        return "USER_POST";
    }
}
