package models;
import java.io.Serializable;
import java.util.Objects;

public class UserPostId implements Serializable {
    private int postId;
    private User user;
    public UserPostId() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPostId)) return false;
        UserPostId that = (UserPostId) o;
        return postId == that.postId && user == that.user;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, user);
    }
}
