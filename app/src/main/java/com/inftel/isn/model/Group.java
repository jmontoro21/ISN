package com.inftel.isn.model;

import java.util.List;
import java.util.Objects;

/**
 * Created by inftel13 on 19/03/15.
 */

public class Group {


    private String id;
    private String admin;
    private String name;
    private String imageUrl;
    private List<User> userList;

    public Group() {

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.admin);
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + Objects.hashCode(this.imageUrl);
        hash = 23 * hash + Objects.hashCode(this.userList);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Group other = (Group) obj;
        if (!Objects.equals(this.admin, other.admin)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.imageUrl, other.imageUrl)) {
            return false;
        }
        if (!Objects.equals(this.userList, other.userList)) {
            return false;
        }
        return true;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public Group(String id, String admin, String name, String imageUrl, List<User> userList) {
        this.id = id;
        this.admin = admin;
        this.name = name;
        this.imageUrl = imageUrl;
        this.userList = userList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<User> getUser() {
        return userList;
    }

    public void setUser(List<User> user) {
        this.userList = user;
    }

}


