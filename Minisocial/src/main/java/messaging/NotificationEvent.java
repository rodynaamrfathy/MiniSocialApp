package messaging;

import java.io.Serializable;

/**
 * NotificationEvent – Represents a notification event that will be sent.
 * 
 * Contains details about the source and target users, the event type, and the message.
 * This class is used for serializing notification data to be sent or processed by messaging systems.
 */
public class NotificationEvent implements Serializable {

    /** ID of the user who triggered the event (source) */
    private Long sourceUserId;

    /** ID of the user who is the recipient of the event (target) */
    private Long targetUserId;

    /** Type of the event (e.g., "FRIEND_REQUEST_RECEIVED", "POST_LIKED") */
    private String eventType;

    /** The message associated with the event */
    private String message;

    /** Default constructor (required for deserialization) */
    public NotificationEvent() {}

    /**
     * Parameterized constructor for initializing the notification event.
     * 
     * @param sourceUserId ID of the source user triggering the event
     * @param targetUserId ID of the target user receiving the notification
     * @param eventType Type of the event (e.g., FRIEND_REQUEST_RECEIVED)
     * @param message The message to send with the notification
     */
    public NotificationEvent(Long sourceUserId, Long targetUserId, String eventType, String message) {
        this.sourceUserId = sourceUserId;
        this.targetUserId = targetUserId;
        this.eventType = eventType;
        this.message = message;
    }

    /** 
     * Gets the source user ID (who triggered the event).
     * 
     * @return The ID of the source user.
     */
    public Long getSourceUserId() {
        return sourceUserId;
    }

    /** 
     * Sets the source user ID (who triggered the event).
     * 
     * @param sourceUserId The ID of the source user.
     */
    public void setSourceUserId(Long sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    /** 
     * Gets the target user ID (who receives the event).
     * 
     * @return The ID of the target user.
     */
    public Long getTargetUserId() {
        return targetUserId;
    }

    /** 
     * Sets the target user ID (who receives the event).
     * 
     * @param targetUserId The ID of the target user.
     */
    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    /** 
     * Gets the event type (e.g., "FRIEND_REQUEST_RECEIVED").
     * 
     * @return The event type as a string.
     */
    public String getEventType() {
        return eventType;
    }

    /** 
     * Sets the event type (e.g., "POST_LIKED").
     * 
     * @param eventType The event type as a string.
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /** 
     * Gets the message of the notification.
     * 
     * @return The message string.
     */
    public String getMessage() {
        return message;
    }

    /** 
     * Sets the message of the notification.
     * 
     * @param message The message string.
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
