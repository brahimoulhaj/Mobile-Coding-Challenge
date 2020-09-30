package com.braoul.github30days;

import java.util.HashMap;
import java.util.Map;

public class Repository {
    private String name;
    private String description;
    private int numStars;
    private String ownerUsername;
    private String ownerAvatar;

    public Repository(String name, String description, int numStars, String ownerUsername, String ownerAvatar) {
        this.name = name;
        this.description = description;
        this.numStars = numStars;
        this.ownerUsername = ownerUsername;
        this.ownerAvatar = ownerAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumStars() {
        return numStars;
    }

    public void setNumStars(int nStars) {
        this.numStars = nStars;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getOwnerAvatar() {
        return ownerAvatar;
    }

    public void setOwnerAvatar(String ownerAvatar) {
        this.ownerAvatar = ownerAvatar;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("description", description);
        params.put("numStars", numStars);
        params.put("ownerUsername", ownerUsername);
        params.put("ownerAvatar", ownerAvatar);
        return params;
    }
}
