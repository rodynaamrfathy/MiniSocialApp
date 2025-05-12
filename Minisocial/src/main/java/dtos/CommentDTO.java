package dtos;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import models.Comment;

/**
 * The CommentDTO (Data Transfer Object) class represents the data structure used for transferring
 * comment information. It is a simplified version of the Comment model, intended for use in API responses.
 *
 * This DTO includes the comment's ID, creator's details, post ID, content, and timestamp.
 */
public class CommentDTO {
    
    /**
     * The unique identifier for the comment.
     * This corresponds to the comment ID in the database.
     */
    private int commentId;
    
    /**
     * The unique identifier of the creator (User) of the comment.
     * This represents the ID of the user who created the comment.
     */
    private Long creatorId;
    
    /**
     * The name of the user who created the comment.
     * This is used to display the creator's name in the DTO.
     */
    private String creatorName;
    
    /**
     * The unique identifier of the post the comment belongs to.
     * This corresponds to the post ID in the database.
     */
    private int postId;
    
    /**
     * The content of the comment.
     * This holds the actual text of the comment.
     */
    private String content;
    
    /**
     * The timestamp of when the comment was created.
     * This represents the date and time the comment was posted.
     */
    private Date timestamp;

    // Getters and Setters

    /**
     * Gets the unique identifier of the comment.
     * @return the comment ID.
     */
    public int getCommentId() { 
        return commentId; 
    }

    /**
     * Sets the unique identifier of the comment.
     * @param commentId the comment ID to set.
     */
    public void setCommentId(int commentId) { 
        this.commentId = commentId; 
    }

    /**
     * Gets the unique identifier of the creator of the comment.
     * @return the creator ID.
     */
    public Long getCreatorId() { 
        return creatorId; 
    }

    /**
     * Sets the unique identifier of the creator of the comment.
     * @param creatorId the creator ID to set.
     */
    public void setCreatorId(Long creatorId) { 
        this.creatorId = creatorId; 
    }

    /**
     * Gets the name of the creator of the comment.
     * @return the creator name.
     */
    public String getCreatorName() { 
        return creatorName; 
    }

    /**
     * Sets the name of the creator of the comment.
     * @param creatorName the creator name to set.
     */
    public void setCreatorName(String creatorName) { 
        this.creatorName = creatorName; 
    }

    /**
     * Gets the unique identifier of the post the comment belongs to.
     * @return the post ID.
     */
    public int getPostId() { 
        return postId; 
    }

    /**
     * Sets the unique identifier of the post the comment belongs to.
     * @param postId the post ID to set.
     */
    public void setPostId(int postId) { 
        this.postId = postId; 
    }

    /**
     * Gets the content of the comment.
     * @return the comment content.
     */
    public String getContent() { 
        return content; 
    }

    /**
     * Sets the content of the comment.
     * @param content the content to set.
     */
    public void setContent(String content) { 
        this.content = content; 
    }

    /**
     * Gets the timestamp of when the comment was created.
     * @return the comment timestamp.
     */
    public Date getTimestamp() { 
        return timestamp; 
    }

    /**
     * Sets the timestamp of when the comment was created.
     * @param timestamp the timestamp to set.
     */
    public void setTimestamp(Date timestamp) { 
        this.timestamp = timestamp; 
    }

    // Static Methods for Conversion

    /**
     * Converts a Comment object into a CommentDTO.
     * This method extracts the relevant fields from the Comment entity and populates the DTO.
     * @param comment the Comment object to convert.
     * @return a new CommentDTO object populated with data from the provided Comment.
     */
    public static CommentDTO fromComment(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setCommentId(comment.getCommentId());
        dto.setContent(comment.getContent());
        dto.setTimestamp(comment.getTimestamp());

        if (comment.getCreator() != null) {
            dto.setCreatorId(comment.getCreator().getUserId());
            dto.setCreatorName(comment.getCreator().getUserName());
        }

        if (comment.getPost() != null) {
            dto.setPostId(comment.getPost().getPostId());
        }

        return dto;
    }

    /**
     * Converts a list of Comment objects into a list of CommentDTO objects.
     * This method maps each Comment entity into a CommentDTO.
     * @param comments the list of Comment objects to convert.
     * @return a list of CommentDTO objects.
     */
    public static List<CommentDTO> fromCommentList(List<Comment> comments) {
        return comments.stream()
                .map(CommentDTO::fromComment)
                .collect(Collectors.toList());
    }
}
