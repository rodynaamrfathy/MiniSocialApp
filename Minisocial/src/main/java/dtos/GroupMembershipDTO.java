package dtos;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import enums.GroupMemberShipStatusEnum;
import models.GroupMembership;

/**
 * GroupMembershipDTO represents a Data Transfer Object for Group Membership entities.
 * It is used to transfer simplified group membership data between layers (e.g., from Entity to API response).
 * This class includes user details, group details, role, membership status, and join date.
 * 
 * Provides static methods for converting Entity objects to DTO objects.
 */
public class GroupMembershipDTO {

    /** Unique identifier for the GroupMembership record */
    private Long id;

    /** Unique identifier for the associated User */
    private Long userId;

    /** Display name of the User */
    private String userName;

    /** Unique identifier for the associated Group */
    private Long groupId;

    /** Display name of the Group */
    private String groupName;

    /** Role of the user within the group (e.g., 'Member', 'Admin') */
    private String role;

    /** Current membership status (e.g., 'approved', 'pending') */
    private String status;

    /** Date when the user joined the group */
    private Date joinedDate;

    /**
     * Default no-args constructor.
     * Required for frameworks that instantiate DTO objects.
     */
    public GroupMembershipDTO() {}

    /**
     * Parameterized constructor to initialize all fields.
     * 
     * @param id GroupMembership record ID
     * @param userId User ID
     * @param userName User Name
     * @param groupId Group ID
     * @param groupName Group Name
     * @param role User Role in Group
     * @param status Membership Status
     * @param joinedDate Date when the user joined the group
     */
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
 
    /** @return GroupMembership ID */
    public Long getId() {
        return id;
    }

    /** @param id GroupMembership ID */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return User ID */
    public Long getUserId() {
        return userId;
    }

    /** @param userId User ID */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /** @return User Name */
    public String getUserName() {
        return userName;
    }

    /** @param userName User Name */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** @return Group ID */
    public Long getGroupId() {
        return groupId;
    }

    /** @param groupId Group ID */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /** @return Group Name */
    public String getGroupName() {
        return groupName;
    }

    /** @param groupName Group Name */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /** @return Role in Group */
    public String getRole() {
        return role;
    }

    /** @param role Role in Group */
    public void setRole(String role) {
        this.role = role;
    }

    /** @return Membership Status */
    public String getStatus() {
        return status;
    }

    /** @param status Membership Status */
    public void setStatus(String status) {
        this.status = status;
    }

    /** @return Date of Joining Group */
    public Date getJoinedDate() {
        return joinedDate;
    }

    /** @param joinedDate Date of Joining Group */
    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }


    /**
     * Converts a GroupMembership entity into a GroupMembershipDTO.
     * Extracts required fields from related User and Group entities.
     * 
     * @param membership GroupMembership entity instance
     * @return Corresponding GroupMembershipDTO
     */
    public static GroupMembershipDTO fromEntity(GroupMembership membership) {
        return new GroupMembershipDTO(
                membership.getId(),
                membership.getUser().getUserId(),
                membership.getUser().getUserName(),
                membership.getGroup().getGroupId(),
                membership.getGroup().getGroupName(),
                membership.getRole(),
                membership.getStatus().toString(),
                membership.getJoinedDate()
        );
    }

    /**
     * Converts a list of GroupMembership entities into a list of GroupMembershipDTOs.
     * Only includes memberships with 'approved' status.
     * 
     * @param memberships List of GroupMembership entities
     * @return Filtered and mapped list of GroupMembershipDTOs
     */
    public static List<GroupMembershipDTO> fromEntityList(List<GroupMembership> memberships) {
        return memberships.stream()
                .filter(m -> m.getStatus() == GroupMemberShipStatusEnum.approved) // Include only approved memberships
                .map(GroupMembershipDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
