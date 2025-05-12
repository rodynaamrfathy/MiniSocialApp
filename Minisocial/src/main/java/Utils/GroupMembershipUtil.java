package Utils;

import enums.GroupMemberShipStatusEnum;
import models.Group;
import models.GroupMembership;
import models.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class GroupMembershipUtil {

    @PersistenceContext
    private EntityManager em;

    public void addInitialMembership(Long userId, Group group) {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("Invalid User ID");
        }

        GroupMembership membership = new GroupMembership();
        membership.setGroup(group);
        membership.setUser(user);
        membership.setRole("admin");
        membership.setStatus(GroupMemberShipStatusEnum.approved);
        membership.setJoinedDate(new Date());

        em.persist(membership);
    }

    public List<String> validateJoinRequest(User user, Group group, boolean alreadyMember) {
        List<String> errors = new ArrayList<>();

        if (user == null) {
            errors.add("User not found.");
        }
        if (group == null) {
            errors.add("Group not found.");
        }
        if (alreadyMember) {
            errors.add("User is already a member or has a pending request.");
        }

        return errors;
    }

    public List<String> validateLeaveRequest(GroupMembership membership) {
        List<String> errors = new ArrayList<>();

        if (membership == null) {
            errors.add("Membership not found.");
        } else {
            switch (membership.getStatus()) {
                case rejected:
                    errors.add("User cannot leave the group because they have been rejected.");
                    break;
                case pending:
                    errors.add("User cannot leave the group because their request is still pending.");
                    break;
                default:
                    break;
            }
        }

        return errors;
    }
}
