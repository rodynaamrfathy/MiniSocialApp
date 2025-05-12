package dtos;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import models.UserPost;

/**
 * UserPostDTO represents a Data Transfer Object (DTO) for User Posts.
 * This object is used to transfer post data from the back end to the client.
 * It contains basic post details like content, image URL, publish date, and user information.
 */
public class UserPostDTO {

    // The unique identifier for the post.
    private Integer id;
    
    // The content text of the post.
    private String content;
    
    // The URL of the image associated with the post (if any).
    private String imageUrl;
    
    // The date and time when the post was published.
    private Date publishDate;
    
    // The unique identifier of the user who created the post.
    private Long userId; 
    
    // The name of the user who created the post.
    private String userName; 

    // Getters and Setters for the class attributes

    /**
     * Returns the ID of the post.
     * @return The ID of the post.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the post.
     * @param id The ID to set for the post.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the content of the post.
     * @return The content of the post.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content for the post.
     * @param content The content to set for the post.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Returns the image URL associated with the post.
     * @return The image URL of the post.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the image URL for the post.
     * @param imageUrl The image URL to set for the post.
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Returns the publish date of the post.
     * @return The publish date of the post.
     */
    public Date getPublishDate() {
        return publishDate;
    }

    /**
     * Sets the publish date for the post.
     * @param publishDate The publish date to set for the post.
     */
    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * Returns the ID of the user who created the post.
     * @return The user ID of the post creator.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID for the post.
     * @param userId The user ID to set for the post creator.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the user name of the post creator.
     * @return The user name of the post creator.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name for the post creator.
     * @param userName The user name to set for the post creator.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Static method to convert a UserPost object to a UserPostDTO object.
     * This method extracts necessary data from the UserPost and maps it to a DTO.
     * 
     * @param userPost The UserPost object to convert.
     * @return The corresponding UserPostDTO object.
     */
    public static UserPostDTO fromUserPost(UserPost userPost) {
    	
        UserPostDTO dto = new UserPostDTO();
        
        if (userPost != null) {
            dto.setId(userPost.getPostId());  
            dto.setContent(userPost.getContent());  
            dto.setImageUrl(userPost.getImageUrl());  
            dto.setPublishDate(userPost.getPublishDate());  

          
            if (userPost.getUser() != null) {
               
                dto.setUserId(userPost.getUser().getUserId());  
                dto.setUserName(userPost.getUser().getUserName()); 
            }
        }
        
        
        return dto;
    }

    /**
     * Static method to convert a list of UserPost objects to a list of UserPostDTO objects.
     * This method maps each UserPost in the list to its corresponding DTO.
     * 
     * @param posts The list of UserPost objects to convert.
     * @return The list of UserPostDTO objects.
     */
    public static List<UserPostDTO> fromUserPostList(List<UserPost> posts) {
        
        return posts.stream()
                    .map(UserPostDTO::fromUserPost)  
                    .collect(Collectors.toList());  
    }
}
