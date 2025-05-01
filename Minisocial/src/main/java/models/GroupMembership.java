package models;

import java.util.Date;

import javax.persistence.*;


import enums.StatusEnum;

@Entity
@IdClass(GroupMembershipId.class)
public class GroupMembership {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long membershipId;
	
    @Id
    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;

    @Id
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(length = 50)
    private String role;

    @Temporal(TemporalType.DATE)
    private Date joinedDate;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

}
