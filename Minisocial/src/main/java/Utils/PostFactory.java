package Utils;

import models.Group;
import models.GroupPost;
import models.Post;
import models.User;
import models.UserPost;

import java.util.Date;

/**
 * 	 PostFactory – Factory for creating different types of Posts.
 * 
 * 	 Responsibilities:
 * - Create UserPost or GroupPost with appropriate properties.
 * - Requires validated input for post creation.
 */
public class PostFactory {

    /**
     * Factory method to create a Post.
     * 
     * This method creates either a UserPost or a GroupPost based on the given post type.
     * It also sets the common fields for the post, such as content, image URL, publish date, and likes count.
     * 
     * @param type      The type of the post ("user" or "group").
     * @param author    The author of the post (User).
     * @param content   The content of the post.
     * @param imageUrl  An optional image URL attached to the post.
     * @param group     The group associated with the post (only for GroupPost).
     * @return A constructed Post object (either UserPost or GroupPost).
     * @throws IllegalArgumentException If the post type is invalid or if a group is required for a GroupPost but not provided.
     */
    public static Post createPost(String type, User author, String content, String imageUrl, Group group) {
        Post post;

       
        switch (type.toLowerCase()) {
            case "user":
        
                UserPost userPost = new UserPost();
                userPost.setUser(author); 
                post = userPost;
                break;

            case "group":
          
                if (group == null) {
                    throw new IllegalArgumentException("Group is required for GroupPost.");
                }
           
                GroupPost groupPost = new GroupPost();
                groupPost.setUser(author);
                groupPost.setGroup(group); 
                post = groupPost;
                break;

            default:
       
                throw new IllegalArgumentException("Invalid post type: " + type);
        }

     
        post.setContent(content); 
        post.setImageUrl(imageUrl); 
        post.setPublishDate(new Date()); 
        post.setLikesCount(0); 

        return post; 
    }
}
