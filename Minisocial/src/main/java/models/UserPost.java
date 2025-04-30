package models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "posts")
//@IdClass(UserPostId.class)
public class UserPost extends Post{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int postId;
	
    //@Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    

	
}
