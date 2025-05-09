package models;

import java.util.List;
import java.util.stream.Collectors;

public class LikeDTO {
    private int likeId;
    private Long userId;
    private String userName;
    private int postId;

    // Getters and Setters
    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
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

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    // Static method to convert a Like to a LikeDTO
    public static LikeDTO fromLike(Like like) {
        LikeDTO dto = new LikeDTO();
        dto.setLikeId(like.getLikeId());

        if (like.getUser() != null) {
            dto.setUserId(like.getUser().getUserId());
            dto.setUserName(like.getUser().getUserName());
        } else {
            dto.setUserId(null);
            dto.setUserName(null);
        }

        if (like.getPost() != null) {
            dto.setPostId(like.getPost().getPostId());
        } else {
            dto.setPostId(-1); // Handle null post or invalid post ID if necessary
        }

        return dto;
    }

    // Static method to convert a list of Like to a list of LikeDTO
    public static List<LikeDTO> fromLikeList(List<Like> likes) {
        return likes.stream()
                    .map(LikeDTO::fromLike)
                    .collect(Collectors.toList());
    }
}
