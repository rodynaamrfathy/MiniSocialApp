package models;

import javax.persistence.*;
import java.util.Date;
import enums.FriendshipStatus;

/**
 * 📨 FriendshipRequests Entity – Relationships Summary 📮
 * 
 * Represents a pending or past request:
 * 
 * [User] ---1---------------------< [FriendshipRequests] >---------------------1--- [User]
 *       (as requester)                                       (as receiver)
 * 
 * Each row is a friend request from one user to another.
 * Status is tracked using Enum: PENDING, ACCEPTED, REJECTED.
 */

@Entity
public class FriendshipRequests {

    // 🔑 Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int friendship_request_id;

    // 👈 Many requests by same requester (User)
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    // 👉 Many requests received by same receiver (User)
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    // 📌 Enum for request status (PENDING, ACCEPTED, REJECTED)
    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    // ⏰ Timestamp of the request
    @Temporal(TemporalType.DATE)
    private Date timestamp;

    // ✅ All Getters & Setters...
    
    public FriendshipRequests () {}

    public int getFriendshipRequestId() {
        return friendship_request_id;
    }
    
    public void setFriendshipRequestId(int friendship_request_id) {
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

    // 📝 toString() method for a readable string representation of the FriendshipRequests entity
    @Override
    public String toString() {
        return "FriendshipRequests{" +
               "friendship_request_id=" + friendship_request_id +
               ", requester=" + (requester != null ? requester.getUserName() : "null") +
               ", receiver=" + (receiver != null ? receiver.getUserName() : "null") +
               ", status=" + status +
               ", timestamp=" + timestamp +
               '}';
    }
}
