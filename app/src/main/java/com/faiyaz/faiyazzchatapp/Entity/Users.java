package com.faiyaz.faiyazzchatapp.Entity;

public class Users {

    String profileImg,email,username,password,userId,lastMessage,status;


    public Users() {

    }

    public Users(String userId, String username, String password, String email, String profileImg, String status) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.profileImg = profileImg;
        this.status = status;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
