package com.baro.models;

import java.net.URI;
import java.util.ArrayList;
import java.util.UUID;

public class User {

    private final UUID userUUID;
    private final String username;


    private URI photoThumbnail;
    private ArrayList<User> followings;
    private ArrayList<Course> courses;

    // TODO discuss properties


    public User(UUID userUUID, String username) {
        this.userUUID = userUUID;
        this.username = username;
    }


    public String getUsername() {
        return username;
    }
}
