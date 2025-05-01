package models;

import java.io.Serializable;
import java.util.Objects;

public class GroupPostId implements Serializable {
    private int groupPostId;
    private int user;
    private int group;

    public GroupPostId() {}



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupPostId)) return false;
        GroupPostId that = (GroupPostId) o;
        return groupPostId == that.groupPostId &&
               user == that.user &&
               group == that.group;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupPostId, user, group);
    }
}
