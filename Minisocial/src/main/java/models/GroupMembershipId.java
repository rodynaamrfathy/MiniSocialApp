package models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GroupMembershipId implements Serializable {
    @Column(name = "group_id")
    private Long groupId;
    
    @Column(name = "user_id")
    private Long userId;

	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@Override
	public int hashCode() {
		return Objects.hash(groupId, userId);
	}
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

    // Getters and Setters, equals() and hashCode() methods
}
