package models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 💡 Represents a social group in the platform.
 */
@Entity
@Table(name = "Groups") // ✅ Use a non-reserved name
public class Group {

    /** 🔑 Primary key for the group */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    /** 🏷️ Name of the group */
    @Column(nullable = false)
    private String groupName;

    /** 📝 Description of the group */
    private String description;

    /** 🔓 Whether the group is open to join without approval */
    @Column(nullable = false)
    private Boolean isOpen;

    /** 📝 Posts belonging to the group */
    @OneToMany(mappedBy = "group")
    private Set<GroupPost> groupPosts = new HashSet<>();

    /** 👥 Membership records for this group */
    @OneToMany(mappedBy = "group")
    private Set<GroupMembership> memberships = new HashSet<>();

    /** 🔐 Admins for the group */
    @OneToMany
    @JoinColumn(name = "group_id")  // Explicit join column
    private Set<GroupAdmins> groupAdmins = new HashSet<>();

    // 🛠️ Getters & Setters

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Set<GroupPost> getGroupPosts() {
        return groupPosts;
    }

    public void setGroupPosts(Set<GroupPost> groupPosts) {
        this.groupPosts = groupPosts;
    }

    public Set<GroupAdmins> getGroupAdmins() {
        return groupAdmins;
    }

    public void setGroupAdmins(Set<GroupAdmins> groupAdmins) {
        this.groupAdmins = groupAdmins;
    }

    public Set<GroupMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<GroupMembership> memberships) {
        this.memberships = memberships;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", description='" + description + '\'' +
                ", isOpen=" + isOpen +
                '}';
    }
}
