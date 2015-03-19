package com.inftel.isn.model;

import org.w3c.dom.Comment;

import java.util.ArrayList;

/**
 * Created by inftel13 on 19/03/15.
 */
public class PrivateComments {

    private String id;
    private String userEmail;
    private ArrayList<Comment> commentsList;

    public PrivateComments() {
        this.commentsList = new ArrayList<>();
    }

    public PrivateComments(String id, String userEmail, ArrayList<Comment> commentsList) {
        this.id = id;
        this.userEmail = userEmail;
        this.commentsList = commentsList;
    }

    public ArrayList<Comment> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(ArrayList<Comment> commentsList) {
        this.commentsList = commentsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
