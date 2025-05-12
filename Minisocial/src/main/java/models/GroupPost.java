package models;

import javax.persistence.*;

/**
 * GroupPost – Represents a post within a group.
 * 
 * GroupPost is a subclass of Post that adds group-related functionality.
 * It includes references to the author (User) and the group the post belongs to.
 * 
 * Relationships:
 * - Many-to-One (N:1) with User (author)
 * - Many-to-One (N:1) with Group (belongs to)
 */
@Entity
@Table(name = "groupposts")
public class GroupPost extends Post {

    /**
     * Author of the post.
     * 
     * This relationship is mapped as a Many-to-One with the User entity.
     */
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    /**
     * Group in which the post was published.
     * 
     * This relationship is mapped as a Many-to-One with the Group entity.
     */
    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;

    /**
     * Returns the user (author) of the post.
     * 
     * @return the author of the post.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user (author) of the post.
     * 
     * @param user the author to set.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the group the post belongs to.
     * 
     * @return the group the post belongs to.
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Sets the group the post belongs to.
     * 
     * @param group the group to set.
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Returns a string identifier for the type of post.
     * 
     * @return the string "GROUP_POST".
     */
    @Override
    public String getType() {
        return "GROUP_POST"; // Return a string identifier specific to group posts
    }

    /**
     * Returns a string representation of the GroupPost object.
     * 
     * @return a string representing the GroupPost object.
     */
    @Override
    public String toString() {
        return "GroupPost [user=" + user + ", group=" + group +
               ", postId=" + getPostId() + ", content=" + getContent() + "]";
    }
}
