package models;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "post_type")
public abstract class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int postId;
	
	@Temporal(TemporalType.DATE)
	protected Date publishDate;
	
	
	protected String imageUrl;
	
	@Column(length = 50)
	protected String content;
	
 
    @OneToMany(mappedBy = "post")  // 'post' in Comment class
    private Set<Comment> comments;  // List of comments related to this post
    
	
    
    @OneToMany(mappedBy = "post")  // 'post' in Like class
    private Set<Like> likes;  // List of likes related to this post
   
	
	protected int likesCount;

}
