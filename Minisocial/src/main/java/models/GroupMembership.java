package models;

import javax.persistence.*;
import java.util.Date;
import enums.GroupMemberShipStatusEnum;

/**
 * GroupMembership Entity
 * 
 * Represents the membership of a user in a group, with a unique membershipId as primary key.
 */
@Entity
public class GroupMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    
    private String role;  // Role like 'admin', 'member', etc.
    
    @Enumerated(EnumType.STRING)
    private GroupMemberShipStatusEnum status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date joinedDate;

    // Getters and Setters


    public Group getGroup() {
        return group;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public GroupMemberShipStatusEnum getStatus() {
        return status;
    }

    public void setStatus(GroupMemberShipStatusEnum status) {
        this.status = status;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }
}
