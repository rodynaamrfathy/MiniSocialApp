package models;

import javax.persistence.*;

import java.util.Date;

import enums.FriendshipStatus;

@Entity
public class FriendshipRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int friendship_request_id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester; 

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver; 

    @Enumerated(EnumType.STRING) 
    private FriendshipStatus status;
    
    @Temporal(TemporalType.DATE)
    private Date timestamp;
    
    public FriendshipRequests () {}

    public int getFriendshipRequestId() {
        return friendship_request_id;
    }
    
    public void getFriendshipRequestId(int friendship_request_id) {
        this.friendship_request_id = friendship_request_id;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }
    
    public Date getTimeStamp() {
		return timestamp;
	}

	public void setTimeStamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}

