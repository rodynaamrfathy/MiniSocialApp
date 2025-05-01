package models;

import java.util.Set;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Group {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long groupId;
	
	@Column
	private String groupName;
	
	@Column
	private Boolean isOpen;
	
    @ManyToMany
    @JoinTable(
    		name="GroupMembership",
    		joinColumns=@JoinColumn(name="groupId"),
    		inverseJoinColumns=@JoinColumn(name="userId"))
    private Set<User> members;
	
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }
    
    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }


    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", isOpen=" + isOpen +
                ", membersCount=" + (members != null ? members.size() : 0) +
                '}';
    }
}
