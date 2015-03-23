package com.inftel.isn.model;



import java.util.ArrayList;

/**
 * Created by inftel13 on 19/03/15.
 */
public class GroupComments {

    private String id;
    private Group group;
    private ArrayList<Comment> commentsList;

    public GroupComments() {
        this.commentsList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public ArrayList<Comment> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(ArrayList<Comment> commentsList) {
        this.commentsList = commentsList;
    }

}
