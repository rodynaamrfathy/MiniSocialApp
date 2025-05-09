package models;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CommentDTO {
    private int commentId;
    private Long creatorId;
    private String creatorName;
    private int postId;
    private String content;
    private Date timestamp;

    // Getters and Setters
    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public Long getCreatorId() { return creatorId; }
    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }

    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

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

    public static List<CommentDTO> fromCommentList(List<Comment> comments) {
        return comments.stream()
                .map(CommentDTO::fromComment)
                .collect(Collectors.toList());
    }
}

