package com.inftel.isn.model;

import java.util.Date;
import java.util.Objects;

/**
 * Created by inftel13 on 19/03/15.
 */
public class Comment {

    private String text;
    private Date creationDate;
    private User author;
    private String imageUrl;
    private String videoUrl;

    public Comment() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.text);
        hash = 13 * hash + Objects.hashCode(this.creationDate);
        hash = 13 * hash + Objects.hashCode(this.author.getGoogleId());
        hash = 13 * hash + Objects.hashCode(this.imageUrl);
        hash = 13 * hash + Objects.hashCode(this.videoUrl);
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
        final Comment other = (Comment) obj;
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        if (!Objects.equals(this.creationDate, other.creationDate)) {
            return false;
        }
        if (!Objects.equals(this.author.getGoogleId(), other.author.getGoogleId())) {
            return false;
        }
        if (!Objects.equals(this.imageUrl, other.imageUrl)) {
            return false;
        }
        if (!Objects.equals(this.videoUrl, other.videoUrl)) {
            return false;
        }
        return true;
    }
}
