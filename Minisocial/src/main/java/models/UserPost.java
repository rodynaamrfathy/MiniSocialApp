package models;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 🧾 UserPost – Relationships Summary 🧾
 * 
 * Subclass of Post:
 *   🔹 Inherits: comments, likes, content, imageUrl, publishDate
 * 
 * [UserPost] ---N-------------------> [User]      (N : 1)
 *                 (author)
 *
 * Legend:
 *   -->  Many-to-One (N:1)
 */

@Entity
public class UserPost extends Post {

    /** 👤 Author of the user post */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
