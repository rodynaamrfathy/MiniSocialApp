package models;

import javax.persistence.*;

import java.util.Date; 

@Entity
public class Friendships {
  
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int friendship_id ;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;
    
    @Temporal(TemporalType.DATE)
    private Date since;
    
    public Friendships() {}
    
    public int getFriendshipId() {
		return friendship_id;
	}

	public void setFriendshipId(int friendship_id) {
		this.friendship_id = friendship_id;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getFriend() {
		return friend;
	}
	
	public void setFriend(User friend) {
		this.friend = friend;
	}
	
	public Date getSince() {
		return since;
	}

	public void setSince(Date since) {
		this.since = since;
	}
    
}
