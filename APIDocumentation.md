# 📘 MiniSocialApp REST API Documentation

> Base URL: `http://localhost:8080/Minisocial/api/`

---

## 🔐 Authentication

### `POST /auth/login`

Login with credentials.

```json
Request Body:
{
  "username": "string",
  "password": "string"
}
```

**Responses**

* `200 OK` – Returns JWT token
* `401 Unauthorized` – Invalid credentials

---

### `POST /auth/register`

Register a new user.

```json
Request Body:
{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

**Responses**

* `201 Created` – User registered
* `400 Bad Request` – Validation error

---

## 👤 UserResource

### `GET /users`

List all users.

### `GET /users/{id}`

Get user by ID.

### `POST /users`

Create a user.

### `PUT /users/{id}`

Update a user.

### `DELETE /users/{id}`

Delete a user.

### `GET /users/{id}/friends`

Get a user's friends.

### `GET /users/{id}/notifications`

Get notifications for a user.

---

## 🧑‍🤝‍🧑 FriendshipResource

### `POST /friends/request/{targetUserId}`

Send a friend request.

### `POST /friends/accept/{requestId}`

Accept a friend request.

### `POST /friends/reject/{requestId}`

Reject a friend request.

### `DELETE /friends/remove/{friendId}`

Remove a friend.

---

## 📝 PostResource

### `GET /posts`

List public posts.

### `GET /posts/{id}`

Get a post.

### `POST /posts`

Create a post.

### `PUT /posts/{id}`

Update a post.

### `DELETE /posts/{id}`

Delete a post.

---

## 💬 CommentResource

### `GET /posts/{postId}/comments`

Get all comments on a post.

### `POST /posts/{postId}/comments`

Add a comment.

```json
Request Body:
{
  "content": "string"
}
```

### `DELETE /comments/{commentId}`

Delete a comment.

---

## ❤️ LikeResource

### `POST /posts/{postId}/like`

Like a post.

### `DELETE /posts/{postId}/unlike`

Unlike a post.

---

## 👥 GroupResource

### `GET /groups`

Get all groups.

### `GET /groups/{id}`

Get group details.

### `POST /groups`

Create a group.

### `PUT /groups/{id}`

Update group.

### `DELETE /groups/{id}`

Delete group.

---

## 🤝 GroupMembershipResource

### `POST /groups/{groupId}/join`

Join a group.

### `POST /groups/{groupId}/leave`

Leave a group.

### `GET /groups/{groupId}/members`

List group members.

### `POST /groups/{groupId}/promote/{userId}`

Promote user to admin.

---

## 🧾 GroupPostResource

### `GET /groups/{groupId}/posts`

List all posts in a group.

### `POST /groups/{groupId}/posts`

Create a group post.

### `DELETE /groups/{groupId}/posts/{postId}`

Delete a group post.

---

## 🔔 NotificationResource

### `GET /notifications`

Get all notifications.

### `GET /notifications/unread`

Get unread notifications.

### `POST /notifications/mark-as-read/{id}`

Mark a notification as read.

---

## 🛠️ AdminUserResource

### `GET /admin/users`

Admin list all users.

### `DELETE /admin/users/{id}`

Admin delete user.

### `GET /admin/logs`

View all activity logs.

---

## 📚 ActivityLogResource

### `GET /activity`

Get all activity logs.

### `GET /activity/{userId}`

Get logs for a specific user.

