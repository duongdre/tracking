package com.example.dss.project.Models;

public class User {
    private String id;
    private String username;
    private String URL;

    public User(String id, String username, String URL) {
        this.id = id;
        this.username = username;
        this.URL = URL;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
