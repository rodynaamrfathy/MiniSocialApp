package dtos;

import models.Group;

/**
 * GroupDTO
 *
 * Data Transfer Object (DTO) representing the Group entity.
 * Used to transfer group data between layers (Entity ➡ DTO ➡ API).
 *
 * Fields:
 * - groupId (Long): Unique identifier of the group.
 * - groupName (String): Name of the group.
 * - description (String): Brief description about the group.
 * - isOpen (Boolean): Flag indicating if the group is open to join without approval.
 */
public class GroupDTO {

    /**
     * Unique identifier of the group.
     */
    private Long groupId;

    /**
     * Name of the group.
     */
    private String groupName;

    /**
     * Description of the group.
     */
    private String description;

    /**
     * Determines if the group is open for direct joining.
     * true = open, false = requires admin approval.
     */
    private Boolean isOpen;

    // ============================
    // Getters and Setters
    // ============================

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

    // ============================
    // Conversion Methods
    // ============================

    /**
     * Converts a Group entity to GroupDTO.
     *
     * @param group The Group entity object.
     * @return GroupDTO representation of the Group entity.
     */
    public static GroupDTO fromGroup(Group group) {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setGroupId(group.getGroupId());
        groupDTO.setGroupName(group.getGroupName());
        groupDTO.setDescription(group.getDescription());
        groupDTO.setIsOpen(group.getIsOpen());
        return groupDTO;
    }
}
