package models;

import java.io.Serializable;
import java.util.Objects;

public class GroupMembershipId implements Serializable {
    private Group group;
    private User user;
    private int membershipId;
    

    public GroupMembershipId() {}



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupMembershipId)) return false;
        GroupMembershipId that = (GroupMembershipId) o;
        return group == that.group &&
        		user == that.user &&
        		membershipId == that.membershipId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, user, membershipId);
    }
}
