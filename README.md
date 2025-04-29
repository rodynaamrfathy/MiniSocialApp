# MiniSocial - Social Networking Application

**MiniSocial** is a lightweight social networking platform built with modern enterprise Java technologies. It offers a robust and modular architecture for managing user interactions, group activities, posts, and real-time notifications. Designed for scalability and collaboration, MiniSocial provides a seamless user experience and strong role-based access control.

---

## 🚀 Features

### 1. User Management
- **Registration**: Secure user sign-up with email and password.
- **Login**: User authentication with token-based session management.
- **Profile Management**: Edit name, bio, email, password, and assign roles (`user` or `admin`).

### 2. Connection Management
- **Friend Requests**: Send, receive, accept, or reject requests.
- **Connection List**: View current connections and their profiles.

### 3. Post Management
- **Create Posts**: Share updates with text, links, or image URLs.
- **Timeline Feed**: See posts from yourself and your connections.
- **Post Editing**: Update or delete your own posts.
- **Engagement**: Like and comment on posts to drive interaction.

### 4. Group Management
- **Create Groups**: Build communities with specific topics and interests.
- **Membership Requests**: Join open groups or request access to private ones.
- **Group Posts**: Post within groups, visible only to group members.
- **Admin Roles**: Group creators can manage posts, promote admins, or delete the group.

### 5. Real-Time Notifications (JMS)
- Receive instant alerts for:
  - New friend requests
  - Likes or comments on your posts
  - Group join/leave events
- Built using standardized event objects and message-driven beans.

### 6. Security and Authentication
- **JAAS Integration**: Secure role-based access.
- **Access Control**:
  - **Admins**: Full access to manage users and content.
  - **Users**: Access limited to personal and social features.
- Unauthorized access triggers a `403 Forbidden` response.

---

## ⚙️ Tech Stack

| Layer                | Technology Used          |
|---------------------|--------------------------|
| Business Logic      | EJB (Enterprise JavaBeans) |
| Persistence         | JPA (Java Persistence API) |
| Transactions        | JTA (Java Transaction API) |
| API Layer           | JAX-RS (Java RESTful Services) |
| Authentication      | JAAS (Java Authentication and Authorization) |
| Messaging           | JMS (Java Messaging Service) |

---

## 📦 Project Structure

```plaintext
src/
├── model/                # JPA Entities
├── service/              # Stateless EJBs (Business Logic)
├── controller/           # REST Endpoints (JAX-RS)
├── security/             # JAAS Configuration
├── messaging/            # JMS Producers & Consumers
└── util/                 # Helper Classes & Configurations
```

---

## 🧪 API Endpoints

### ➤ User Registration
- **POST** `/api/register`
```json
{
  "email": "user@example.com",
  "password": "UserPassword123",
  "name": "John Doe",
  "bio": "Hello! I'm a new user.",
  "role": "user"
}
```

### ➤ User Login
- **POST** `/api/login`
```json
{
  "email": "user@example.com",
  "password": "UserPassword123"
}
```

### ➤ Update Profile
- **PUT** `/api/users/{user_id}/update`
```json
{
  "name": "Updated Name",
  "bio": "Updated bio.",
  "email": "updated@example.com",
  "password": "NewPass123"
}
```

> 💡 A full Postman collection with ready-made requests and sample bodies is included for API testing.

---

## 🧠 Clean Architecture

- Fully modular design ensuring separation of concerns:
  - Models for data
  - Services for business logic
  - Controllers for routing and requests
  - Messaging for asynchronous flows
  - Security module for role validation

---

## 🔐 Access & Permissions

- All admin-only routes are secured with JAAS.
- Role checks are enforced at both the API and service levels.
- Unauthorized access is safely rejected.

---

## 📬 Notifications Format

A standardized JSON object is used for all JMS events:
```json
{
  "eventType": "FriendRequestReceived",
  "targetUser": "john.doe",
  "timestamp": "2025-04-22T13:00:00Z",
  "message": "You have received a new friend request from Jane."
}
```

---

## 🌟 Enhancements

- **Media Attachments**: Post with image URLs.
- **User Search & Suggestions**: Discover new friends by email or mutual connections.
- **Activity Logs**: Track user activity (likes, comments, posts) using JMS.

---

## 📌 Notes

- Maintain consistent coding standards.
- Follow best practices for security, scalability, and maintainability.
- Each team member should understand all modules to ensure collaborative ownership.

---

Let me know if you’d like me to generate a matching `Postman` collection file or add deployment instructions (e.g., with GlassFish or WildFly).
