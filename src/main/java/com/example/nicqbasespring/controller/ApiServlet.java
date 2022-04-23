package com.example.nicqbasespring.controller;

import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.service.UserService;
import com.example.nicqbasespring.util.UserUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;


@RestController
@Slf4j
@Tag(name = "http api",description = "http api 接口")
public class ApiServlet {
    private UserService userService;
    @Autowired(required = false)
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    private RedisTemplate redisTemplate;
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


//    @RequestMapping(value = "/**",method = RequestMethod.POST)
//    public void apitpreprocessing(HttpSession httpSession){
//        System.out.println(httpSession.getAttribute("Logined").toString());
//        if (httpSession.getAttribute("Logined")==null){
//            httpSession.setAttribute("Logined",false);
//        }
//    }
    @Operation(
            summary = "user longin api",
            parameters = {
                    @Parameter(name="userName",description = "用户名"),
                    @Parameter(name="passwd",description = "密码")
            }
    )


    @RequestMapping(value="/login",method = RequestMethod.POST)
    public String Login(@RequestBody @NotNull User user, @NotNull HttpSession httpSession){
        log.info(user.getUserName()+user.getPasswd());
        log.info(String.valueOf(httpSession.getMaxInactiveInterval()));
//        WebSocketConnect.logout(httpSession);
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
            httpSession.setMaxInactiveInterval(-1);
            return data.toString();
        }
        else{
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
    public String getfriendlist(@NotNull HttpSession httpSession) {
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

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(HttpSession httpSession){
        log.info("httpsession"+httpSession.getId());
        redisTemplate.getClientList();
        redisTemplate.opsForValue().set("countUserNum", WebSocketConnect.onlineusernum());
        return (Objects.requireNonNull(redisTemplate.opsForValue().get("countUserNum"))).toString();
    }

    @RequestMapping(value = "/searchuser",method = RequestMethod.POST)
    public String  searchuser(@RequestBody String data,HttpSession httpSession){
//        if(redisTemplate.opsForHash().values("onlineuser").contains(httpSession.getId()))
//        {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsondata = objectMapper.valueToTree(data);
//            log.info(jsondata.toString());
//            return "Unkow";
//        }else
//        {
//            return "user invalid";
//        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsondata = objectMapper.valueToTree(data);
        log.info(jsondata.toString());
        return "Unkow";
    }
}