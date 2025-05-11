package models;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

/**
 * 📸 Post (Abstract) – Relationships Summary 📘
 *
 * Superclass for all post types (UserPost, GroupPost)
 * 
 * [Post] <---1----------------------< [Comment]      (1 : N)
 * [Post] <---1----------------------< [Like]         (1 : N)
 * 
 * Child Types:
 *   🔹 [UserPost] – Post by a user
 *   🔹 [GroupPost] – Post within a group
 * 
 * Inheritance: JOINED (Table-per-subclass) with discriminator column "post_type"
 *
 * Legend:
 *   -->  One-to-Many (1:N)
 */


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "post_type")
public abstract class Post {

    /** 🔑 Unique ID for each post */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    /** 📅 Date when the post was published */
    @Temporal(TemporalType.DATE)
    protected Date publishDate;

    @Override
	public String toString() {
		return "Post [postId=" + postId + ", publishDate=" + publishDate + ", imageUrl=" + imageUrl + ", content="
				+ content + ", comments=" + comments + ", likes=" + likes + ", likesCount=" + likesCount + "]";
	}

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

	/** 🖼️ Optional image URL attached to the post */
    protected String imageUrl;

    /** 📝 Content of the post (Max 50 chars) */
    @Column(length = 50)
    protected String content;

    /** 💬 One-to-many relationship with Comment (reverse mapped by 'post') */
    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;

    /** ❤️ One-to-many relationship with Like (reverse mapped by 'post') */
    @OneToMany(mappedBy = "post")
    private Set<Like> likes;

    /** 🔢 Cached count of likes for optimization */
    protected int likesCount;
    
    protected int commentsCount;
    
    /** 👤 Abstract getter for the post author (User) */
    public abstract User getUser();

	public abstract Group getGroup();

	public abstract void setUser(User user);

	public abstract String getType();

}