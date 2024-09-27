package edu.ewubd.bloodplus;

import com.google.firebase.database.ServerValue;

public class Comment {

    private String comment, userID, userName, userImg;
    private Object timeStamp;

    public Comment(){

    }

    public Comment(String comment, String userID, String userName, String userImg) {
        this.comment = comment;
        this.userID = userID;
        this.userName = userName;
        this.userImg = userImg;

        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
