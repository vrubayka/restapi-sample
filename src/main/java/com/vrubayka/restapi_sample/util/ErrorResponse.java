package com.vrubayka.restapi_sample.util;

import com.vrubayka.restapi_sample.model.User;

public class ErrorResponse {
    private String message;
    private User user;

    // Constructor
    public ErrorResponse(String message, User user) {
        this.message = message;
        this.user = user;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter for user
    public User getUser() {
        return user;
    }

    // Setter for user
    public void setUser(User user) {
        this.user = user;
    }
}

