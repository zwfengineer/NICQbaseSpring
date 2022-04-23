package com.example.nicqbasespring.entries;

import com.fasterxml.jackson.annotation.JacksonInject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Component
@Data
public class User implements Serializable {
    String UserName ;
    private String Passwd;
    @JacksonInject("UserGender")
    @Value("保密")
    private String Gender;
    String UID;
    @JacksonInject("UserAvatar")
    @Value("0")
    String Avatar;
    String Phonenum;
    private List<Map<String,String>> Friends;
    @Autowired(required = false)
    public void setPhonenum(String phonenum) {
        Phonenum = phonenum;
    }
    @Value("normal")
    @JacksonInject("UserPermission")
    private String Permission;
    @Autowired(required = false)
    public void setGender(String gender) {
        Gender = gender;
    }
    public String getPermission() {
        return Permission;
    }
    @Autowired(required = false)
    @Value("normal")
    public void setPermission(String permission) {
        Permission = permission;
    }
    public String getUserName() {
        return UserName;
    }
    @Autowired(required = false)
    public void setUserName(String userName) {
        UserName = userName;
    }
    @Autowired(required = false)
    public void setUID(String UID) {
        this.UID = UID;
    }
    @Autowired(required = false)
    public void setPasswd(String passwd) {
        Passwd = passwd;
    }
    @Autowired(required = false)
    public void setAvatar(String Avatar) {
        this.Avatar = Avatar;
    }
}
