package models;

import java.util.Date;

import javax.persistence.*;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class Post {
	
	@Temporal(TemporalType.DATE)
	protected Date publishDate;
	
	
	protected String imageUrl;
	
	@Column(length = 50)
	protected String content;
	
    /*
    @OneToMany(mappedBy = "post")  // 'post' in Comment class
    private List<Comment> comments;  // List of comments related to this post
    */
	
    /*
    @OneToMany(mappedBy = "post")  // 'post' in Like class
    private List<Like> likes;  // List of likes related to this post
    */
	
	protected int likesCount;

}
