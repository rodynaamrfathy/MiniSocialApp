package Utils;

import models.Group;
import models.GroupPost;
import models.Post;
import models.User;
import models.UserPost;

import java.util.Date;

/**
 * 🏭 PostFactory – Factory for creating different types of Posts.
 * 
 * ✅ Responsibilities:
 * - Create UserPost or GroupPost with appropriate properties
 * - Requires validated input
 */
public class PostFactory {

    /**
     * Factory method to create a Post.
     *
     * @param type      "user" or "group"
     * @param author    the author of the post (User)
     * @param content   post content
     * @param imageUrl  optional image URL
     * @param group     the group (only for GroupPost)
     * @return constructed Post object
     */
    public static Post createPost(String type, User author, String content, String imageUrl, Group group) {
        Post post;

        switch (type.toLowerCase()) {
            case "user":
                UserPost userPost = new UserPost();
                userPost.setUser(author); // Set user (author) for the post
                post = userPost;
                break;

            case "group":
                if (group == null) {
                    throw new IllegalArgumentException("Group is required for GroupPost.");
                }
                GroupPost groupPost = new GroupPost();
                groupPost.setUser(author); // Set user (author) for the post
                groupPost.setGroup(group); // Set group for the post
                post = groupPost;
                break;

            default:
                throw new IllegalArgumentException("Invalid post type: " + type);
        }

        // Set common fields for both UserPost and GroupPost
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setPublishDate(new Date());
        post.setLikesCount(0);

        return post;
    }
}
