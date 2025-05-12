package models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "likes") // ✅ Corrected table name to plural, consistent with 'comments'
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id") // ✅ Added consistent column naming
    private int likeId;

    // One-to-Many relationship with UserPost (Nullable for GroupPost usage)
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    private UserPost post;

    // One-to-Many relationship with GroupPost (Nullable for UserPost usage)
    @ManyToOne
    @JoinColumn(name = "group_post_id", nullable = true)
    private GroupPost groupPost;

    // User who liked the post
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date(); // ✅ Added timestamp like Comment class

    // ➡️ Getters and Setters

    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    public UserPost getPost() {
        return post;
    }

    public void setPost(UserPost post) {
        this.post = post;
    }

    public GroupPost getGroupPost() {
        return groupPost;
    }

    public void setGroupPost(GroupPost groupPost) {
        this.groupPost = groupPost;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
