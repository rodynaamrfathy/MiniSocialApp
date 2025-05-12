package models;

import javax.persistence.*;
import java.time.Instant;

/**
 *Represents a notification entry in the system.
 * 
 * Notifications inform users about various events like friend requests,
 * post likes, or group-related activities.
 * 
 * Associations:
 * - Belongs to {@link User} (recipient of the notification)
 */
@Entity
@Table(name = "notifications")
public class NotificationEntity {

    /** Primary key: Unique identifier for the notification. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    /** ID of the user who receives the notification. */
    private Long targetUserId;

    /** ID of the user who triggered the notification event. */
    private Long sourceUserId;

    /** Type of event that triggered the notification (e.g., FRIEND_REQUEST_RECEIVED, POST_LIKED, GROUP_JOINED). */
    private String eventType;

    /** Category of the notification (e.g., SOCIAL, POST, GROUP). */
    private String category;

    /** The notification message content. */
    @Column(length = 1000)
    private String message;

    /** Reference to a related entity (e.g., postId, commentId, groupId). */
    private Long referenceId;

    /** Time stamp when the notification was created. */
    private Instant timestamp;

    /** Status indicating whether the notification has been read. */
    private Boolean isRead = false;

    /** The user entity associated with this notification. */
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    /** Default constructor initializes the time stamp to current instant. */
    public NotificationEntity() {
        this.timestamp = Instant.now();
    }

    // === Getters and Setters ===

    /** Returns the notification ID. */
    public Long getId() {
        return notificationId;
    }

    /** Sets the notification ID. */
    public void setId(Long id) {
        this.notificationId = id;
    }

    /** Returns the ID of the user who receives the notification. */
    public Long getTargetUserId() {
        return targetUserId;
    }

    /** Sets the ID of the user who receives the notification. */
    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    /** Returns the ID of the user who triggered the notification. */
    public Long getSourceUserId() {
        return sourceUserId;
    }

    /** Sets the ID of the user who triggered the notification. */
    public void setSourceUserId(Long sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    /** Returns the event type of the notification. */
    public String getEventType() {
        return eventType;
    }

    /** Sets the event type of the notification. */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /** Returns the category of the notification. */
    public String getCategory() {
        return category;
    }

    /** Sets the category of the notification. */
    public void setCategory(String category) {
        this.category = category;
    }

    /** Returns the message content of the notification. */
    public String getMessage() {
        return message;
    }

    /** Sets the message content of the notification. */
    public void setMessage(String message) {
        this.message = message;
    }

    /** Returns the reference ID related to the notification (post, group, etc.). */
    public Long getReferenceId() {
        return referenceId;
    }

    /** Sets the reference ID related to the notification. */
    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    /** Returns the time stamp of the notification. */
    public Instant getTimestamp() {
        return timestamp;
    }

    /** Sets the time stamp of the notification. */
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    /** Returns whether the notification has been read. */
    public Boolean getIsRead() {
        return isRead;
    }

    /** Sets the read status of the notification. */
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
