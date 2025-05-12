package models;

import javax.persistence.*;
import java.util.Date;
import enums.FriendshipStatus;

/**
 * Represents a friendship request between two users.
 * 
 * This entity maps the relationship of friend requests:
 *  One user (requester) sends requests to others (receiver).
 *  Tracks status (PENDING, ACCEPTED, REJECTED) and timestamp.
 * 
 * Associations:
 *  Many requests can be sent by one requester.
 *  Many requests can be received by one receiver.
 */
@Entity
public class FriendshipRequests {

    /** Primary key: Unique ID of the friendship request. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int friendship_request_id;

    /** The user who sent the friendship request. */
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    /** The user who received the friendship request. */
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    /** Current status of the request (PENDING, ACCEPTED, REJECTED). */
    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    /** The date when the friendship request was created. */
    @Temporal(TemporalType.DATE)
    private Date timestamp;

    /** Default constructor. */
    public FriendshipRequests() {}

    // === Getters and Setters ===

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

    /** Returns a string representation of the friendship request. */
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
