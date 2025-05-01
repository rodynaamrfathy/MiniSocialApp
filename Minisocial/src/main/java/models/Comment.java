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
    
    @ManyToOne
    @JoinColumn(name = "post_id") // foreign key from Post hierarchy
    private Post post; // polymorphic

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "content", length = 50)
    private String content;

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