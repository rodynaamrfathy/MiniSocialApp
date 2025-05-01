package models;

import javax.persistence.*;


@Entity
@Table(name = "groupposts")
public class GroupPost extends Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupPostId;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    
    
    /*
     * the connection with the group
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    */
}
