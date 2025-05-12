package models;

import javax.persistence.*;
import dtos.GroupAdmins;

import java.util.HashSet;
import java.util.Set;

/**
 *  Represents a social group within the platform.
 * 
 * A group allows users to post content, manage memberships,
 * and define administrators. Groups can be open (anyone can join)
 * or closed (requires approval).
 * 
 * Associations:
 * - Has many {@link GroupPost} (posts in the group)
 * - Has many {@link GroupMembership} (memberships)
 * - Has many {@link GroupAdmins} (administrators)
 */
@Entity
@Table(name = "Groups") // ✅ Avoid SQL reserved keyword conflict
public class Group {

    /** Primary key: Unique identifier for the group. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    /** The name/title of the group. */
    @Column(nullable = false)
    private String groupName;

    /** A short description of the group's purpose. */
    private String description;

    /** Defines if the group is open to public joining (without approval). */
    @Column(nullable = false)
    private Boolean isOpen;

    /** Posts that belong to this group. */
    @OneToMany(mappedBy = "group")
    private Set<GroupPost> groupPosts = new HashSet<>();

    /** Membership records for users in this group. */
    @OneToMany(mappedBy = "group")
    private Set<GroupMembership> memberships = new HashSet<>();

    /** Admins who manage this group. */
    @OneToMany
    @JoinColumn(name = "group_id")  // Explicit foreign key join
    private Set<GroupAdmins> groupAdmins = new HashSet<>();

    // === Getters and Setters ===

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

    /** Returns a string representation of the group. */
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
