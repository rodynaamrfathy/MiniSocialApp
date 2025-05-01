package models;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class UserPost extends Post{
	
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
	
}
