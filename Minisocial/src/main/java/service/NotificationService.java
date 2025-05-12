package service;

import models.NotificationEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import messaging.NotificationEvent;

import java.time.Instant;
import java.util.List;

@Stateless
public class NotificationService {

    @PersistenceContext
    private EntityManager em;
    
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


    public List<NotificationEntity> getNotificationsForUser(Long userId) {
        return em.createQuery(
                "SELECT n FROM NotificationEntity n WHERE n.targetUserId = :userId ORDER BY n.timestamp DESC",
                NotificationEntity.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    public void markAsRead(Long id) {
        NotificationEntity notification = em.find(NotificationEntity.class, id);
        if (notification != null) {
            notification.setIsRead(true);
            em.merge(notification);
        }
    }
}
