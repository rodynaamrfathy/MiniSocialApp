package models;

import javax.persistence.*;

import java.util.Date;

/**
 * 💬 Comment – Relationships Summary 💬
 *
 * Represents a user comment on a post.
 * 
 * [Comment] ---N-------------------> [Post]       (N : 1)
 * [Comment] ---N-------------------> [User]       (N : 1) (creator)
 * 
 * Fields:
 *   - content
 *   - timestamp
 *
 * Legend:
 *   -->  Many-to-One (N:1)
 */

@Entity
@Table(name = "comments")
public class Comment {

    /** 🔑 Unique comment ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentId;

    /** 🧱 Target post (polymorphic - can be any Post subtype) */
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    /** 👤 Author of the comment */
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    /** 📝 Text content of the comment (Max 50 chars) */
    @Column(name = "content", length = 50)
    private String content;

    /** 🕒 Timestamp when the comment was made */
    @Column(name = "timestamp")
    @Temporal(TemporalType.DATE)
    private Date timestamp;

    // Getters and Setters
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

} 