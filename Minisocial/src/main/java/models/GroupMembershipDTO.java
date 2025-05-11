package models;

import java.util.Date;

public class GroupMembershipDTO {

    private Long id;
    private Long userId;
    private String userName;
    private Long groupId;
    private String groupName;
    private String role;
    private String status;
    private Date joinedDate;

    // Constructors, Getters, and Setters

    public GroupMembershipDTO(Long id, Long userId, String userName, Long groupId, String groupName, String role, String status, Date joinedDate) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.groupId = groupId;
        this.groupName = groupName;
        this.role = role;
        this.status = status;
        this.joinedDate = joinedDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }

    // Utility method to convert GroupMembership entity to DTO
    public static GroupMembershipDTO fromGroupMembership(GroupMembership groupMembership) {
        return new GroupMembershipDTO(
            groupMembership.getId(),
            groupMembership.getUser().getUserId(),
            groupMembership.getUser().getUserName(),
            groupMembership.getGroup().getGroupId(),
            groupMembership.getGroup().getGroupName(),
            groupMembership.getRole(),
            groupMembership.getStatus().toString(),
            groupMembership.getJoinedDate()
        );
    }
}
