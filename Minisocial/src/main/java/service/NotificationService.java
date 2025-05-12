package service;

import models.NotificationEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import messaging.NotificationEvent;

import java.time.Instant;
import java.util.List;

/**
 * Service class responsible for managing notifications.
 * This class provides methods to create, retrieve, and update notifications.
 */
@Stateless
public class NotificationService {

    /**
     * Injected EntityManager for database operations.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Creates a new notification based on the given event details.
     * 
     * @param event the event that triggers the creation of a notification.
     * This event contains information such as the source and target user IDs,
     * event type, and message for the notification.
     */
    public void createNotification(NotificationEvent event) {
        NotificationEntity notification = new NotificationEntity();
        notification.setSourceUserId(event.getSourceUserId());
        notification.setTargetUserId(event.getTargetUserId());
        notification.setEventType(event.getEventType());
        notification.setMessage(event.getMessage());
        notification.setIsRead(false);
        notification.setTimestamp(Instant.now());

        em.persist(notification);
    }

    /**
     * Retrieves the list of notifications for a specific user.
     * Notifications are ordered by timestamp in descending order.
     * 
     * @param userId the ID of the user whose notifications are to be fetched.
     * @return a list of notifications for the user, ordered by the most recent.
     */
    public List<NotificationEntity> getNotificationsForUser(Long userId) {
        return em.createQuery(
                "SELECT n FROM NotificationEntity n WHERE n.targetUserId = :userId ORDER BY n.timestamp DESC",
                NotificationEntity.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    /**
     * Marks a notification as read.
     * 
     * @param id the ID of the notification to be marked as read.
     * If the notification exists, its "isRead" flag will be set to true.
     */
    public void markAsRead(Long id) {
        NotificationEntity notification = em.find(NotificationEntity.class, id);
        if (notification != null) {
            notification.setIsRead(true);
            em.merge(notification);
        }
    }
}
