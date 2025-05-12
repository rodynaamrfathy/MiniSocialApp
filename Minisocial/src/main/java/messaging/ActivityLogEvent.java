package messaging;

import java.io.Serializable;
import java.time.Instant;

public class ActivityLogEvent implements Serializable {
    private Long userId;
    private String action; // "POSTED", "LIKED", "FRIEND_ADDED"
    private String description;
    private Instant timestamp = Instant.now();

    public ActivityLogEvent(Long userId, String action, String description) {
        this.userId = userId;
        this.action = action;
        this.description = description;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

}

