package com.example.zdemoappo.model;

public class CommentsModel {
    String comment;

    public CommentsModel(String comment) {
        this.comment = comment;
    }

    public CommentsModel() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
