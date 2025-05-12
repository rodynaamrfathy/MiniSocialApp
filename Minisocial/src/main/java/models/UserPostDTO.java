package models;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UserPostDTO {
    private Integer id;
    private String content;
    private String imageUrl;
    private Date publishDate;
    private Long userId; 
    private String userName; 

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    // Static method to convert a UserPost to a UserPostDTO
    public static UserPostDTO fromUserPost(UserPost userPost) {
        UserPostDTO dto = new UserPostDTO();
        if (userPost != null) {
            dto.setId(userPost.getPostId()); 
            dto.setContent(userPost.getContent());
            dto.setImageUrl(userPost.getImageUrl());
            dto.setPublishDate(userPost.getPublishDate());

            // Null checks for User object to prevent NPE
            if (userPost.getUser() != null) {
                dto.setUserId(userPost.getUser().getUserId()); 
                dto.setUserName(userPost.getUser().getUserName());
            }
        }
        return dto;
    }

    
    public static List<UserPostDTO> fromUserPostList(List<UserPost> posts) {
        return posts.stream()
                    .map(UserPostDTO::fromUserPost)
                    .collect(Collectors.toList());
    }
}

