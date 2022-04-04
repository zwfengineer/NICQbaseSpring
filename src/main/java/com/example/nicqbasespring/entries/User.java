package com.example.nicqbasespring.entries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class User {
    String UserName ;
    private String Passwd;
    private String Gender;
    String UID;
    @Value("0")
    String Avatar;
    private List<Object> Friends;
    private List<Object> Messages;

    @Value("normal")
    private String Permission;

    public String getGender() {
        return Gender;
    }
    @Autowired(required = false)
    public void setGender(String gender) {
        Gender = gender;
    }

    public void setMessages(List<Object> messages) {
        Messages = messages;
    }

    public String getPermission() {
        return Permission;
    }

    @Autowired(required = false)
    public void setPermission(String permission) {
        Permission = permission;
    }

    public Boolean addfriend(){
        return null;
    }
    public void removefriend(){

    }
    public String getUserName() {
        return UserName;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserName='" + UserName + '\'' +
                ", Passwd='" + Passwd + '\'' +
                ", Gender='" + Gender + '\'' +
                ", UID='" + UID + '\'' +
                ", Avatar='" + Avatar + '\'' +
                ", Friends=" + Friends +
                ", Messages=" + Messages +
                ", Permission='" + Permission + '\'' +
                '}';
    }

    @Autowired(required = false)
    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUID() {
        return UID;
    }

    @Autowired(required = false)
    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPasswd() {
        return Passwd;
    }

    @Autowired(required = false)
    public void setPasswd(String passwd) {
        Passwd = passwd;
    }

    public List<Object> getFriends() {
        return Friends;
    }


    public void setFriends(List<Object> friends) {
        Friends = friends;
    }

    public void addFriends(String uid){
        Friends.add(uid);
    }

    public void removeFriends(String uid){
        Friends.remove(uid);
    }

    public List<Object> getMessages() {
        return Messages;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String Avatar) {
        this.Avatar = Avatar;
    }
}
