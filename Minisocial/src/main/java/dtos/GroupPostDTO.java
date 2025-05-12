package dtos;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import models.GroupPost;

/**
 * GroupPostDTO – Data Transfer Object for GroupPost.
 * 
 * This DTO class is used to transfer data related to a GroupPost object.
 * It contains the necessary fields and methods to map a GroupPost to its DTO representation.
 */
public class GroupPostDTO {
    
    private Long id;
    private String content;
    private String imageUrl;
    private Date publishDate;
    private Long userId;
    private String userName;
    private Long groupId;
    private String groupName;

    /**
     * Returns the post ID.
     * 
     * @return the post ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the post ID.
     * 
     * @param id the post ID to set.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the content of the post.
     * 
     * @return the content of the post.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the post.
     * 
     * @param content the content to set.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Returns the image URL associated with the post.
     * 
     * @return the image URL.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the image URL for the post.
     * 
     * @param imageUrl the image URL to set.
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Returns the publish date of the post.
     * 
     * @return the publish date.
     */
    public Date getPublishDate() {
        return publishDate;
    }

    /**
     * Sets the publish date of the post.
     * 
     * @param publishDate the publish date to set.
     */
    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * Returns the user ID associated with the post.
     * 
     * @return the user ID.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with the post.
     * 
     * @param userId the user ID to set.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the user name associated with the post.
     * 
     * @return the user name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name associated with the post.
     * 
     * @param userName the user name to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Returns the group ID associated with the post.
     * 
     * @return the group ID.
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * Sets the group ID associated with the post.
     * 
     * @param groupId the group ID to set.
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * Returns the group name associated with the post.
     * 
     * @return the group name.
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the group name associated with the post.
     * 
     * @param groupName the group name to set.
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Converts a GroupPost object to a GroupPostDTO.
     * 
     * @param groupPost the GroupPost object to convert.
     * @return the corresponding GroupPostDTO.
     */
    public static GroupPostDTO fromGroupPost(GroupPost groupPost) {
        GroupPostDTO dto = new GroupPostDTO();
        dto.setId(groupPost.getPostId());
        dto.setContent(groupPost.getContent());
        dto.setImageUrl(groupPost.getImageUrl());
        dto.setPublishDate(groupPost.getPublishDate());

        if (groupPost.getUser() != null) {
            dto.setUserId(groupPost.getUser().getUserId());
            dto.setUserName(groupPost.getUser().getUserName());
        } else {
            dto.setUserId(null);
            dto.setUserName(null);
        }

        if (groupPost.getGroup() != null) {
            dto.setGroupId(groupPost.getGroup().getGroupId());
            dto.setGroupName(groupPost.getGroup().getGroupName());
        } else {
            dto.setGroupId(null);
            dto.setGroupName(null);
        }

        return dto;
    }

    /**
     * Converts a list of GroupPost objects to a list of GroupPostDTOs.
     * 
     * @param posts the list of GroupPost objects to convert.
     * @return the corresponding list of GroupPostDTOs.
     */
    public static List<GroupPostDTO> fromGroupPostList(List<GroupPost> posts) {
        return posts.stream()
                    .map(GroupPostDTO::fromGroupPost)
                    .collect(Collectors.toList());
    }
}
