package models;

import java.util.List;
import java.util.stream.Collectors;

/**
 * LikeDTO is a Data Transfer Object representing Like entity data 
 * with associated user and post information.
 */
public class LikeDTO {

    private int likeId;
    private Long userId;
    private String userName;
    private int postId;
    private String postType; // UserPost or GroupPost

    // Getters and Setters
    public int getLikeId() { return likeId; }
    public void setLikeId(int likeId) { this.likeId = likeId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getPostType() { return postType; }
    public void setPostType(String postType) { this.postType = postType; }

    // ✅ Convert Like entity to DTO
    public static LikeDTO fromLike(Like like) {
        LikeDTO dto = new LikeDTO();
        dto.setLikeId(like.getLikeId());

        if (like.getUser() != null) {
            dto.setUserId(like.getUser().getUserId());
            dto.setUserName(like.getUser().getUserName());
        }

        if (like.getPost() != null) {
            dto.setPostId(like.getPost().getPostId());
            dto.setPostType("UserPost");
        } else if (like.getGroupPost() != null) {
            dto.setPostId(like.getGroupPost().getPostId());
            dto.setPostType("GroupPost");
        } else {
            dto.setPostId(-1);
            dto.setPostType("Unknown");
        }

        return dto;
    }

    // ✅ Convert List of Likes to DTOs
    public static List<LikeDTO> fromLikeList(List<Like> likes) {
        return likes.stream()
                    .map(LikeDTO::fromLike)
                    .collect(Collectors.toList());
    }
}
