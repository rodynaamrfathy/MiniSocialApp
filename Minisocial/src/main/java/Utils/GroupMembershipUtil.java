package Utils;

import models.Group;
import models.GroupMembership;
import models.User;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

/**
 * Utility class for handling group membership-related operations, such as validating join and leave requests.
 * This class provides helper methods for managing memberships in groups and validating requests based on the user and group status.
 */
@Stateless
public class GroupMembershipUtil {

    // JPQL Query Strings

    /**
     * JPQL query to find a specific group membership based on user ID and group ID.
     */
    public static final String FIND_MEMBERSHIP = 
        "SELECT gm FROM GroupMembership gm WHERE gm.user.userId = :userId AND gm.group.groupId = :groupId";

    /**
     * JPQL query to find all pending memberships for a specific group based on the group's ID and membership status.
     * 
     * @see GroupMembership
     */
    public static final String FIND_PENDING_MEMBERSHIPS_FOR_GROUP = 
        "SELECT gm FROM GroupMembership gm WHERE gm.group.groupId = :groupId AND gm.status = :status";

    /**
     * JPQL query to find a pending request for a specific user to join a group, based on user ID, group ID, and request status.
     * 
     * @see GroupMembership
     */
    public static final String FIND_PENDING_REQUEST_FOR_USER = 
        "SELECT gm FROM GroupMembership gm WHERE gm.group.groupId = :groupId AND gm.user.userId = :userId AND gm.status = :status";

    /**
     * Validates a join request to a group.
     * 
     * @param user The user who is requesting to join the group.
     * @param group The group the user is requesting to join.
     * @param alreadyMember Flag indicating whether the user is already a member of the group or has a pending request.
     * @return A list of error messages indicating validation issues.
     */
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

    /**
     * Validates a leave request for a user to leave a group.
     * 
     * @param membership The membership object that represents the user's membership in the group.
     * @return A list of error messages indicating validation issues related to leaving the group.
     */
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
