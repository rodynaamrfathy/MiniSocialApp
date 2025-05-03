package models;

import javax.persistence.*;

/**
 * ❤️ Like – Relationships Summary ❤️
 *
 * Represents a user liking a post.
 * 
 * [Like] ---N----------------------> [Post]       (N : 1)
 * [Like] ---N----------------------> [User]       (N : 1)
 *
 * This is not Many-to-Many directly – uses join entity.
 *
 * Legend:
 *   -->  Many-to-One (N:1)
 */


@Entity
@Table(name = "likes")
public class Like {

    /** 🔑 Unique ID for each like */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likeId")
    private int likeId;

    /** 🧱 Target post which was liked */
    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    /** 👤 User who gave the like */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

} 