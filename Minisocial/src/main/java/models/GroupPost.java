package models;

import javax.persistence.*;


@Entity
@Table(name = "groupposts")
public class GroupPost extends Post {

    
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    
    
    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;
    
}
