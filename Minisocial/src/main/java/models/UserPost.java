package models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
public class UserPost extends Post{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int postId;
	
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    

	
}
