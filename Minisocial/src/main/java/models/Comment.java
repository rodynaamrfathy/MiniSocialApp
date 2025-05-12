package models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentId;

    // One-to-Many relationship with UserPost
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true) // Allowing null for GroupPost usage
    private UserPost post;

    // One-to-Many relationship with GroupPost
    @ManyToOne
    @JoinColumn(name = "group_post_id", nullable = true) // Allowing null for UserPost usage
    private GroupPost groupPost;

    // User creating the comment
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "content", length = 250, nullable = false)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    // Getters and Setters
    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public UserPost getPost() { return post; }
    public void setPost(UserPost post) { this.post = post; }

    public GroupPost getGroupPost() {
        return groupPost;
    }

    public void setGroupPost(GroupPost groupPost) {
        this.groupPost = groupPost;
    }

    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
