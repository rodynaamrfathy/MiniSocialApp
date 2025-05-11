package models;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private Long targetUserId;
    private Long sourceUserId;

    private String eventType;   // FRIEND_REQUEST_RECEIVED, POST_LIKED, GROUP_JOINED
    private String category;    // SOCIAL, POST, GROUP, etc.

    @Column(length = 1000)
    private String message;

    private Long referenceId; // e.g., postId, commentId, groupId

    private Instant timestamp;
    private Boolean isRead = false;
    
    @OneToMany(mappedBy = "notificationsRecived")
    private User userId;

    public NotificationEntity() {
        this.timestamp = Instant.now();
    }

	public Long getId() {
		return notificationId;
	}

	public void setId(Long id) {
		this.notificationId = id;
	}

	public Long getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(Long targetUserId) {
		this.targetUserId = targetUserId;
	}

	public Long getSourceUserId() {
		return sourceUserId;
	}

	public void setSourceUserId(Long sourceUserId) {
		this.sourceUserId = sourceUserId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

}
