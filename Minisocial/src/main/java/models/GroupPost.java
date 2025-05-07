package models;

import javax.persistence.*;

/**
 * 🏘️ GroupPost – Relationships Summary 🏘️
 * 
 * Subclass of Post:
 *   🔹 Inherits: comments, likes, content, imageUrl, publishDate
 * 
 * [GroupPost] ---N-------------------> [User]      (N : 1) (author)
 * [GroupPost] ---N-------------------> [Group]     (N : 1) (belongs to)
 *
 * Legend:
 *   -->  Many-to-One (N:1)
 */

@Entity
@Table(name = "groupposts")
public class GroupPost extends Post {

    
    /** 👤 Author of the post */
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Override
    public String toString() {
        return "GroupPost [user=" + user + ", group=" + group +
               ", postId=" + getPostId() + ", content=" + getContent() + "]";
    }


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	/** 👥 Group in which the post was published */
    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;

    @Override
    public String getType() {
        return "GROUP_POST"; // Return a string identifier specific to group posts
    }
}
