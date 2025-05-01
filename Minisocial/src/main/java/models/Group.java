package models;

import java.util.Set;

import javax.persistence.*;

@Entity
public class Group {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long groupId;
	
	@Column
	private String groupName;
	
	@Column
	private Boolean isOpen;
	
	@OneToMany(mappedBy="postId")
	private Set<GroupPost> groupPosts;
	
    @ManyToMany
    @JoinTable(
    		name="GroupAdmin",
    		joinColumns=@JoinColumn(name="groupId"),
    		inverseJoinColumns=@JoinColumn(name="userId"))
    private Set<User> groupAdmins;
	
    @OneToMany(mappedBy = "group")
    private Set<GroupMembership> memberships;

	
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
    


    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", isOpen=" + isOpen +
                '}';
    }
}
