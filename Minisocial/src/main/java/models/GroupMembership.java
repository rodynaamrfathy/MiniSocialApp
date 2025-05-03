package models;

import java.util.Date;
import javax.persistence.*;
import enums.GroupMemberShipStatusEnum;

/**
 * 🤝 Represents a user’s membership in a group.
 * 
 * 📌 Composite key: (group, user, membershipId) ✅
 * 📌 Includes role, joined date, and status.
 */
@Entity
@IdClass(GroupMembershipId.class)
public class GroupMembership {

    /** 🔑 Unique ID for the membership */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;

    /** 🔗 Associated group */
    @Id
    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;

    /** 👤 Associated user */
    @Id
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    /** 🏷️ Role of the user in the group (e.g., member/admin) */
    @Column(length = 50)
    private String role;

    /** 📅 When the user joined the group */
    @Temporal(TemporalType.DATE)
    private Date joinedDate;

    /** 📌 Current membership status (ACTIVE, PENDING, etc.) */
    @Enumerated(EnumType.STRING)
    private GroupMemberShipStatusEnum status;

    // Getters & Setters...
}
