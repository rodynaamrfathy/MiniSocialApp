package models;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 🌟 Entity representing an admin user of a group.
 * Connects User ↔ Group with admin privileges.
 */
@Entity
@Table(name = "GroupAdmins")
public class GroupAdmins {

    /** 🔑 Primary key for the group admin relationship */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 🔑 Foreign key reference to the user */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** 🔑 Foreign key reference to the group */
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    // 🛠️ Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "GroupAdmins{" +
                "id=" + id +
                ", user=" + user +
                ", group=" + group +
                '}';
    }
}

