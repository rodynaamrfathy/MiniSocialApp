package Utils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

import enums.GroupMemberShipStatusEnum;
import models.Group;
import models.GroupMembership;
import models.User;

@Stateless
public class GroupMembershipUtil {

    @PersistenceContext
    EntityManager em;

    /**
     * 🎯 Adds the creator as an initial approved member in the group.
     * 
     * @param userId The User ID to be added to the group.
     * @param group  The Group entity the user is joining.
     */
    public void addInitialMembership(Long userId, Group group) {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("Invalid User ID");
        }

        // 💬 Create GroupMembership for the user
        GroupMembership membership = new GroupMembership();
        membership.setGroup(group);
        membership.setUser(user);
        membership.setRole("admin");
        membership.setStatus(GroupMemberShipStatusEnum.approved);  // Approving the initial member
        membership.setJoinedDate(new Date());

        // Persist the membership
        em.persist(membership);
    }
}
