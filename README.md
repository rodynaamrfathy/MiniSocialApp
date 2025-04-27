# MiniSocial - Social Networking Application

## 🚀 Project Description
MiniSocial is a lightweight social networking application designed to enhance social interactions in a collaborative environment. The application allows users to connect, share ideas, and create engaging content. Users can register, maintain profiles, post updates, and send friend requests. The framework is built using a clean, modular Java EE architecture, utilizing:

- **EJBs** for business logic
- **JPA** for persistence
- **JTA** for transaction management
- **JAX-RS** for RESTful APIs
- **JAAS** for role-based security
- **JMS** for asynchronous messaging and notifications

---

## 🌟 Core Features

### 1. **User Management** [10 Points] 📄
- **User Registration**: Users can register for an account with a valid email address and password.
- **User Login**: Users can authenticate themselves and log in to access the application.
- **Profile Management**: Users can update their profile information, including their name, bio, email, and password. During profile creation, users can choose a role (user or admin).

### 2. **Connection Management (Social Networking Feature)** [30 Points] 👥
- **Friend Requests**: Users can send and receive friend requests to connect with other users.
- **Manage Connections**: Users can accept or reject pending friend requests.
- **View Connections**: Users can view all their friends and their profiles.

### 3. **Post Management** [40 Points] 📝
- **Create Posts**: Users can create status updates to share with their friends, including attaching images or links.
- **Feed Retrieval**: Users can view a timeline feed that shows their posts and those of their friends.
- **Edit Posts**: Users can update or delete a pre-posted post.
- **Engagement**: Users can like and comment on posts made by friends, facilitating interaction.

### 4. **Groups Management** [40 Points] 👥
- **Create a Group** [5 Points]: Any user can create a new group (e.g., "Java Enthusiasts", "Book Club").
  - A group has: Name, Description, Creator (Group Admin), and List of members.
  - A group can be open or closed (requires admin permission to join).
- **Join & Leave Groups** [15 Points]: Users can request to join a group (pending approval), or join directly if it’s open.
  - Admins can approve or reject membership requests.
  - Notifications are sent via JMS when a member joins or gets approved.
- **Post in Group** [10 Points]: Members can create group-specific posts. Posts are visible only to other members of that group.
- **Group Roles & Permissions** [10 Points]: 
  - Group creator becomes the Group Admin.
  - Admin can promote users to be admins, remove posts or users from the group, and delete the group.
  - Role-based logic is enforced with JAAS or custom EJB checks.

### 5. **Messaging Module - Notifications via JMS** [15 Points] 📬
- **Real-Time Notifications**:
  - Users receive notifications for events such as:
    - When a friend request is received.
    - When a friend likes or comments on their posts.
    - When a comment is made on their posts.
    - When joining or leaving a group.
- **Event Object**: A standardized event object is created to accommodate various event types (like notifications).
  - Verification involves utilizing a message-driven bean or publish/consumer models to print the received event.

### 6. **Authentication and Security** [15 Points] 🔐
- **Role-Based Access Control**:
  - **Admin Role**: Full access for user and content management. Only admins can apply CRUD operations to users' posts and check group admin permissions.
  - **Regular User Role**: Limited access to create posts, manage friends, and view timelines.
- **Access Restrictions**:
  - API endpoints for user management and admin functionalities are restricted to users with the Admin role. Unauthorized access attempts lead to a **403 Forbidden** error.

---

## 🎯 Bonus Features (Optional)
1. **Media Attachments** [2 Points] 📸
   - Allow users to post with image URLs.
2. **User Search & Friend Suggestions** [5 Points] 🔍
   - Users can search other users by name or email.
   - Suggest friends based on mutual connections.
3. **Activity Log** [5 Points] 🧾
   - Show a log of recent actions (posts, likes, friend additions).
   - Use JMS to queue and store logs asynchronously.

---

## 🔧 Tech Stack Overview
- **JPA**: Manage data entities such as `User`, `Post`, `FriendRequest`, and `Group`.
- **EJBs**: Stateless beans that handle various services (e.g., `UserService`, `PostService`, `GroupService`).
- **JAX-RS**: RESTful APIs for interaction with the client-side application.
- **JTA**: Ensures data integrity through transaction management.
- **JAAS**: Security layer for user authentication and role management.
- **JMS**: Messaging system to handle notifications and asynchronous events.

---

## 💻 API References (For Testing)
Here are some API samples you can use for testing:

### 1. **User Registration**
- **URL**: `/api/register`
- **Request Body**:
  ```json
  {
    "email": "user@example.com",
    "password": "UserPassword123",
    "name": "John Doe",
    "bio": "Hello! I'm a new user.",
    "role": "user" // or "admin"
  }
