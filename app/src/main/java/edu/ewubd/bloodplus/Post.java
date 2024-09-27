package edu.ewubd.bloodplus;

import android.net.Uri;

import com.google.firebase.database.ServerValue;

public class Post {
    private String postKey;
    private String title, userName, date, phone, country, city, desc, bloodType, userID, userPhoto;
    private Object timeStamp;


    public Post(String title, String userName, String date, String phone, String country, String city, String desc, String bloodType, String userID, String userPhoto) {
        this.title = title;
        this.userName = userName;
        this.date = date;
        this.phone = phone;
        this.country = country;
        this.city = city;
        this.desc = desc;
        this.bloodType = bloodType;
        this.userID = userID;
        this.userPhoto = userPhoto;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public Post(){

    }

    public Post(String title, String displayName, String date, String phone, String country, String city, String desc, String bloodType, String uid, Uri photoUrl) {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getDesc() {
        return desc;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
