package models;

import javax.persistence.*;


@Entity
@Table(name = "groupposts")
@IdClass(GroupPostId.class)
public class GroupPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupPostId;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    
    /*
     * the connection with the group
    @Id
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    */
}
