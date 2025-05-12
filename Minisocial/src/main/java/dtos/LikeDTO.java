package dtos;

import java.util.List;
import java.util.stream.Collectors;

import models.Like;

/**
 * LikeDTO is a Data Transfer Object (DTO) that represents the data of the Like entity,
 * including associated user and post information.
 * This object is used to transfer data between layers, such as from the service layer to the API layer.
 */
public class LikeDTO {

    // Unique identifier for the Like
    private int likeId;

    // User ID who liked the post
    private Long userId;

    // User's name who liked the post
    private String userName;

    // Post ID related to the Like (could be UserPost or GroupPost)
    private int postId;

    // Type of post (either UserPost or GroupPost)
    private String postType; // UserPost or GroupPost

    // ➡️ Getters and Setters

    /**
     * Retrieves the unique identifier for the Like.
     *
     * @return the Like ID.
     */
    public int getLikeId() { return likeId; }

    /**
     * Sets the unique identifier for the Like.
     *
     * @param likeId the Like ID to set.
     */
    public void setLikeId(int likeId) { this.likeId = likeId; }

    /**
     * Retrieves the user ID of the user who liked the post.
     *
     * @return the user ID of the user who liked the post.
     */
    public Long getUserId() { return userId; }

    /**
     * Sets the user ID of the user who liked the post.
     *
     * @param userId the user ID to set.
     */
    public void setUserId(Long userId) { this.userId = userId; }

    /**
     * Retrieves the user name of the user who liked the post.
     *
     * @return the user name of the user who liked the post.
     */
    public String getUserName() { return userName; }

    /**
     * Sets the user name of the user who liked the post.
     *
     * @param userName the user name to set.
     */
    public void setUserName(String userName) { this.userName = userName; }

    /**
     * Retrieves the post ID related to the Like (either UserPost or GroupPost).
     *
     * @return the post ID related to the Like.
     */
    public int getPostId() { return postId; }

    /**
     * Sets the post ID related to the Like (either UserPost or GroupPost).
     *
     * @param postId the post ID to set.
     */
    public void setPostId(int postId) { this.postId = postId; }

    /**
     * Retrieves the type of the post (either "UserPost" or "GroupPost").
     *
     * @return the post type.
     */
    public String getPostType() { return postType; }

    /**
     * Sets the type of the post (either "UserPost" or "GroupPost").
     *
     * @param postType the post type to set.
     */
    public void setPostType(String postType) { this.postType = postType; }

    // ➡️ Conversion Methods

    /**
     * Converts a Like entity to its corresponding LikeDTO.
     *
     * @param like the Like entity to convert.
     * @return the corresponding LikeDTO.
     */
    public static LikeDTO fromLike(Like like) {
        LikeDTO dto = new LikeDTO();
        dto.setLikeId(like.getLikeId());

        // Set the user details from the Like entity
        if (like.getUser() != null) {
            dto.setUserId(like.getUser().getUserId());
            dto.setUserName(like.getUser().getUserName());
        }

        // Set the post details (either UserPost or GroupPost)
        if (like.getPost() != null) {
            dto.setPostId(like.getPost().getPostId());
            dto.setPostType("UserPost");
        } else if (like.getGroupPost() != null) {
            dto.setPostId(like.getGroupPost().getPostId());
            dto.setPostType("GroupPost");
        } else {
            dto.setPostId(-1); // Unknown post type
            dto.setPostType("Unknown");
        }

        return dto;
    }

    /**
     * Converts a list of Like entities to a list of LikeDTOs.
     *
     * @param likes the list of Like entities to convert.
     * @return the list of corresponding LikeDTOs.
     */
    public static List<LikeDTO> fromLikeList(List<Like> likes) {
        return likes.stream()
                    .map(LikeDTO::fromLike)
                    .collect(Collectors.toList());
    }
}
