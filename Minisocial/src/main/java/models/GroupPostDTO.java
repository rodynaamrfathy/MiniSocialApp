package models;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class GroupPostDTO {
    private Long id;
    private String content;
    private String imageUrl;
    private Date publishDate;
    private Long userId;
    private String userName;
    private Long groupId;
    private String groupName;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
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

    // Static method to convert a GroupPost to a GroupPostDTO
    public static GroupPostDTO fromGroupPost(GroupPost groupPost) {
        GroupPostDTO dto = new GroupPostDTO();
        dto.setId(groupPost.getPostId());
        dto.setContent(groupPost.getContent());
        dto.setImageUrl(groupPost.getImageUrl());
        dto.setPublishDate(groupPost.getPublishDate());

        // Null checks to avoid NullPointerException
        if (groupPost.getUser() != null) {
            dto.setUserId(groupPost.getUser().getUserId());
            dto.setUserName(groupPost.getUser().getUserName());
        } else {
            // Handle the case where user is null
            dto.setUserId(null);
            dto.setUserName(null);
        }

        if (groupPost.getGroup() != null) {
            dto.setGroupId(groupPost.getGroup().getGroupId()); // Assuming the Group entity has a 'getGroupId()' method
            dto.setGroupName(groupPost.getGroup().getGroupName()); // Assuming the Group entity has a 'getGroupName()' method
        } else {
            // Handle the case where group is null
            dto.setGroupId(null);
            dto.setGroupName(null);
        }

        return dto;
    }

    // Convert a list of GroupPost objects to a list of GroupPostDTOs
    public static List<GroupPostDTO> fromGroupPostList(List<GroupPost> posts) {
        return posts.stream()
                    .map(GroupPostDTO::fromGroupPost)
                    .collect(Collectors.toList());
    }
}
