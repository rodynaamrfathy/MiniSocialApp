package messaging;

import java.io.Serializable;

public class NotificationEvent implements Serializable {

    private Long sourceUserId;
    private Long targetUserId;
    private String eventType;
    private String message;

    // Constructors
    public NotificationEvent() {}

    public NotificationEvent(Long sourceUserId, Long targetUserId, String eventType, String message) {
        this.sourceUserId = sourceUserId;
        this.targetUserId = targetUserId;
        this.eventType = eventType;
        this.message = message;
    }

	public Long getSourceUserId() {
		return sourceUserId;
	}

	public void setSourceUserId(Long sourceUserId) {
		this.sourceUserId = sourceUserId;
	}

	public Long getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(Long targetUserId) {
		this.targetUserId = targetUserId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}
