package models;

import java.io.Serializable;
import java.util.Objects;

/**
 * 🔗 Composite primary key for `GroupMembership` entity.
 * 
 * 💡 Consists of:
 * - Group
 * - User
 * - Membership ID
 */
public class GroupMembershipId implements Serializable {

    /** Group part of the key */
    private Group group;

    /** User part of the key */
    private User user;

    /** Membership ID part of the key */
    private int membershipId;

    public GroupMembershipId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupMembershipId)) return false;
        GroupMembershipId that = (GroupMembershipId) o;
        return membershipId == that.membershipId &&
               Objects.equals(group, that.group) &&
               Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, user, membershipId);
    }
}
