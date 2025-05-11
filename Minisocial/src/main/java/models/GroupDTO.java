package models;


/**
 * 🌐 Data Transfer Object (DTO) for Group entity.
 */
public class GroupDTO {

    private Long groupId;
    private String groupName;
    private String description;
    private Boolean isOpen;
    
    // Getters and Setters
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

    // Static method to convert Group entity to GroupDTO
    public static GroupDTO fromGroup(Group group) {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setGroupId(group.getGroupId());
        groupDTO.setGroupName(group.getGroupName());
        groupDTO.setDescription(group.getDescription());
        groupDTO.setIsOpen(group.getIsOpen());
        return groupDTO;
    }
}
