package com.example.nicqbasespring.controller;

import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.service.UserService;
import com.example.nicqbasespring.util.UserUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
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

//    @RequestMapping(value = "/**",method = RequestMethod.POST)
//    public void apitpreprocessing(HttpSession httpSession){
//        System.out.println(httpSession.getAttribute("Logined").toString());
//        if (httpSession.getAttribute("Logined")==null){
//            httpSession.setAttribute("Logined",false);
//        }
//    }
    @RequestMapping(value="/login",method = RequestMethod.POST)
    public String Login(@RequestBody @NotNull User user, @NotNull HttpSession httpSession) throws IOException {
        log.info(user.getUserName()+user.getPasswd());
        log.info(String.valueOf(httpSession.getMaxInactiveInterval()));
        WebSocketConnect.logout(httpSession);
        Object logindata = userService.LoginServer(user);
        if(logindata.getClass().getSimpleName().equals("User"))
        {
            ObjectNode data = (ObjectNode) UserUtil.getUserJson((User)logindata);
            //this user from database!
            //don't trust from front end!
            data.put("Logined",true);
            data.remove("passwd");
            httpSession.setAttribute("Logined",true);
            httpSession.setAttribute("user",user);
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

    @RequestMapping(value="/getfriendlist",method = RequestMethod.POST)
    public String getfriendlist(@NotNull HttpSession httpSession) throws JsonProcessingException {
        List<String> list = UserUtil.asList(httpSession.getAttributeNames().asIterator());
        if (list.contains("Logined")&list.contains("user")){
            Object dataobject = userService.getFriends((User) httpSession.getAttribute("user"));
            System.out.println(dataobject.getClass().toString());
            if (dataobject.getClass().getSimpleName().equals("ArrayNode")){
                return dataobject.toString();
            }
        }
        return "null";
    }

    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public void logout(HttpSession httpSession, HttpServletRequest httpServletRequest) throws  IOException {
        WebSocketConnect.logout(httpSession);
        httpSession.invalidate();
        httpServletRequest.getSession(true);
        log.info(httpSession.getId());
    }

    @RequestMapping(value = "/黑",method = RequestMethod.GET)
    public void test(HttpSession httpSession){
        log.info("httpsession"+httpSession.getId());
    }

}