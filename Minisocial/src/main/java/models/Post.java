package models;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

/**
 * Abstract Entity: Post
 * 
 * Base class for all post types in the system (UserPost, GroupPost).
 * Handles shared attributes and relationships like content, image, likes, and comments.
 * 
 * Relationships:
 * - 1 Post -> N Comments
 * - 1 Post -> N Likes
 * 
 * Inheritance Strategy:
 * - Table-per-subclass (JOINED)
 * - Discriminator column: "post_type"
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "post_type")
public abstract class Post {

    /** Primary Key: Unique identifier for each post */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    /** Date when the post was published */
    @Temporal(TemporalType.DATE)
    protected Date publishDate;

    /** 🖼️ Optional image URL attached to the post */
    protected String imageUrl;

    /** Main content of the post (max 50 characters) */
    @Column(length = 50)
    protected String content;

    /** Comments linked to this post (1:N relationship) */
    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;

    /** Likes linked to this post (1:N relationship) */
    @OneToMany(mappedBy = "post")
    private Set<Like> likes;

    /** Cached count of likes for performance optimization */
    protected int likesCount;

    /** Cached count of comments for performance optimization */
    protected int commentsCount;

    // ====================  Getters & Setters ====================

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    // ====================  Abstract Methods ====================

    /**
     * Retrieves the User (author) associated with this post.
     * @return User entity who created the post
     */
    public abstract User getUser();

    /**
     * Retrieves the Group associated with this post (if applicable).
     * @return Group entity or null if not a group post
     */
    public abstract Group getGroup();

    /**
     * Sets the User (author) for this post.
     * @param user User entity to associate as author
     */
    public abstract void setUser(User user);

    /**
     * Retrieves the post type identifier (e.g., "UserPost", "GroupPost").
     * @return String representing post type
     */
    public abstract String getType();

    // ====================  Utility ====================

    @Override
    public String toString() {
        return "Post [postId=" + postId +
                ", publishDate=" + publishDate +
                ", imageUrl=" + imageUrl +
                ", content=" + content +
                ", comments=" + comments +
                ", likes=" + likes +
                ", likesCount=" + likesCount + "]";
    }
}
