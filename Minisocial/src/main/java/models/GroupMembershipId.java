package models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 📦 GroupMembershipId Entity Class
 * 
 * Represents the **composite primary key** for the `GroupMembership` entity.
 * This key consists of:
 *  - `groupId`: Identifier of the group.
 *  - `userId`: Identifier of the user.
 * 
 * This class is marked as **@Embeddable** to be used in a composite key mapping.
 */
@Embeddable
public class GroupMembershipId implements Serializable {

    /**
     * Group ID
     * Represents the **unique identifier** of the group.
     */
    @Column(name = "group_id")
    private Long groupId;

    /**
     * User ID
     * Represents the **unique identifier** of the user.
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * Gets the group ID.
     * @return groupId - Identifier of the group.
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * Sets the group ID.
     * @param groupId - Identifier of the group.
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * Gets the user ID.
     * @return userId - Identifier of the user.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     * @param userId - Identifier of the user.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * hashCode Implementation
     * Generates a hash code based on groupId and userId.
     * This ensures the composite key's **correct behavior** in hash-based collections.
     * 
     * @return hash code of the composite key.
     */
    @Override
    public int hashCode() {
        return Objects.hash(groupId, userId);
    }

    /**
     * equals Implementation
     * Compares this composite key with another object.
     * Two keys are equal if their groupId and userId are equal.
     * 
     * @param obj - Object to compare with.
     * @return true if both keys are equal; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GroupMembershipId other = (GroupMembershipId) obj;
        return Objects.equals(groupId, other.groupId) && Objects.equals(userId, other.userId);
    }

}
