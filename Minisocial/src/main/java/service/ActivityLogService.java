package service;

import messaging.ActivityLogEvent;
import models.ActivityLog;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ActivityLogService {

    @PersistenceContext
    private EntityManager em;

    public void logActivity(ActivityLogEvent event) {
        ActivityLog log = new ActivityLog();
        log.setUserId(event.getUserId());
        log.setAction(event.getAction());
        log.setDescription(event.getDescription());
        log.setTimestamp(event.getTimestamp());
        em.persist(log);
    }
    
    public List<ActivityLog> getLogsForUser(Long userId) {
        return em.createQuery(
            "SELECT a FROM ActivityLog a WHERE a.userId = :userId ORDER BY a.timestamp DESC",
            ActivityLog.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    public List<ActivityLog> getAllLogs() {
        return em.createQuery(
            "SELECT a FROM ActivityLog a ORDER BY a.timestamp DESC",
            ActivityLog.class)
            .getResultList();
    }

}
