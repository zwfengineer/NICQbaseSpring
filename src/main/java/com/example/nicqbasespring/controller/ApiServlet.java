package com.example.nicqbasespring.controller;

import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.service.UserService;
import com.example.nicqbasespring.util.UserUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.logging.Logger;


@RestController
public class ApiServlet {
    private UserService userService;
    @Autowired(required = false)
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    private Logger log;
    @Autowired(required = false)
    public void setLog(Logger log) {
        this.log = log;
    }
    @RequestMapping(value="/login",method = RequestMethod.POST)
    public String Login(@RequestBody @NotNull User user, @NotNull HttpSession httpSession){
        log.info(user.getUserName()+user.getPasswd());
        log.info(String.valueOf(httpSession.getMaxInactiveInterval()));
        Object logindata = userService.LoginServer(user);
        if(logindata.getClass().getSimpleName().equals("User"))
        {
            ObjectNode data = (ObjectNode) UserUtil.getUserJson((User)logindata);
            //this user from database!
            //don't trust from front end!
            data.put("Logined",true);
            httpSession.setAttribute("Logined",true);
            log.info(httpSession.getId());
            log.info(data.toString());
            return data.toString();
        }else{
            return (String)logindata ;
        }
    }
    @RequestMapping(value="/register",method = RequestMethod.POST)
    public String Register(@RequestBody @NotNull User user){
        log.info(user.getUserName()+user.getPasswd()+user.getPermission());
        Object data = userService.RegisterUser(user);
        if (data.getClass().getSimpleName().equals("User")){
            ObjectNode userNode = (ObjectNode) UserUtil.getUserJson((User) data);
            userNode.put("Registed",true);
            log.info(userNode.toString());
            return userNode.toString();
        }else {
            return (String) data;
        }
    }
}