package com.inftel.isn.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by inftel13 on 19/03/15.
 */

public class Group implements Serializable{


    private String id;
    private String admin;
    private String name;
    private String imageUrl;
    private ArrayList<User> usersList = new ArrayList<>();

    public Group() {

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.admin);
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + Objects.hashCode(this.imageUrl);
        hash = 23 * hash + Objects.hashCode(this.usersList);
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
        if (!Objects.equals(this.usersList, other.usersList)) {
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

    public Group(String id, String admin, String name, String imageUrl, ArrayList<User> usersList) {
        this.id = id;
        this.admin = admin;
        this.name = name;
        this.imageUrl = imageUrl;
        this.usersList = usersList;
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

    public ArrayList<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList<User> usersList) {
        this.usersList = usersList;
    }

    public void removeUserList(int position) {
        this.usersList.remove(position);
    }

    public void addUserToList(User user) {
        this.usersList.add(user);
    }

    public void removeUserFromList(User user) { this.usersList.remove(user);}

}


