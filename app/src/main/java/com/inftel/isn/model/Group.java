package com.inftel.isn.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by inftel13 on 19/03/15.
 */

public class Group implements Parcelable{


    private String id;
    private String admin;
    private String name;
    private String imageUrl;
    private List<User> user = new ArrayList<>();

    public Group() {

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.admin);
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + Objects.hashCode(this.imageUrl);
        hash = 23 * hash + Objects.hashCode(this.user);
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
        if (!Objects.equals(this.user, other.user)) {
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

    public Group(String id, String admin, String name, String imageUrl, ArrayList<User> user) {
        this.id = id;
        this.admin = admin;
        this.name = name;
        this.imageUrl = imageUrl;
        this.user = user;
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
        return user;
    }

    public void setUsersList(ArrayList<User> usersList) {
        this.user = user;
    }

    public void removeUserList(int position) {
        this.user.remove(position);
    }

    public void addUserToList(User user) {
        this.user.add(user);
    }


    public void removeUserFromList(User user) { this.user.remove(user);}

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {

        out.writeString(id);
        out.writeString(admin);
        out.writeString(name);
        out.writeString(imageUrl);
        out.writeList(user);
    }
    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>(){
        public Group createFromParcel(Parcel in){
            return new Group(in);
        }
        public Group[] newArray (int size){
            return new Group[size];
        }

    };
    private Group (Parcel in){
        id = in.readString();
        admin = in.readString();
        name = in.readString();
        imageUrl = in.readString();
        user = in.readArrayList(null);

    }
}


