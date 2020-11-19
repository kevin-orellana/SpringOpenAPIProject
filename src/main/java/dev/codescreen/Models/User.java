package dev.codescreen.Models;

import java.lang.reflect.Type;
import java.util.UUID;

/*
 * Hold details related to a user.
 * This object must be as lightweight as possible.
 */
public class User {
    private Type type;
    private UUID userUuid;

    private String name;
    private String email;

    // Current project UUID
    private String userId;

    public User(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
