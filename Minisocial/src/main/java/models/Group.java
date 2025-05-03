package models;

import java.util.Set;

import javax.persistence.*;

/**
 * 💡 Represents a social group in the platform.
 * 
 * 📌 Relations:
 * - One group has many `GroupPost`s ✅
 * - One group can have many `GroupMembership`s ✅
 * - Many-to-Many with `User` for group admins ✅
 */
@Entity
public class Group {

    /** 🔑 Primary key for the group */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    /** 🏷️ Name of the group */
    @Column
    private String groupName;

    /** 🔓 Whether the group is open to join without approval */
    @Column
    private Boolean isOpen;

    /** 📝 Posts belonging to the group */
    @OneToMany(mappedBy = "group")  // FIXED: should be mapped by "group" in GroupPost
    private Set<GroupPost> groupPosts;

    /** 👑 Users who are admins of this group */
    @ManyToMany
    @JoinTable(
        name = "GroupAdmin",
        joinColumns = @JoinColumn(name = "groupId"),
        inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private Set<User> groupAdmins;

    /** 👥 Membership records for this group */
    @OneToMany(mappedBy = "group")
    private Set<GroupMembership> memberships;

    // Getters & Setters...

    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", isOpen=" + isOpen +
                '}';
    }
}
