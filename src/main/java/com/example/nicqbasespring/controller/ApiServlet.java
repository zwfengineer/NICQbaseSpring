package com.example.nicqbasespring.controller;

import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.exception.WebSocketCloseCode;
import com.example.nicqbasespring.service.HistoryMessageService;
import com.example.nicqbasespring.service.PostSerivce;
import com.example.nicqbasespring.service.UserService;
import com.example.nicqbasespring.util.UserUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.*;


@RestController
@Slf4j
@AllArgsConstructor
@NonNull
@Tag(name = "http api",description = "http api 接口")
public class ApiServlet {
    private HistoryMessageService historyMessageService;
    private UserService userService;
    private RedisTemplate<String,Object> redisTemplate;
    private PostSerivce postSerivce;
    private ObjectMapper objectMapper;
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
//        WebSocketConnect.logout(httpSession);

        Object logindata = userService.LoginServer(user,httpSession);
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


    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public void logout(HttpSession httpSession, HttpServletRequest httpServletRequest) throws IOException {
//        用户退出接口
        httpSession.setAttribute("Logined",false);
        WebSocketConnect.logout(httpSession,new CloseReason(WebSocketCloseCode.USER_PROACTIVELY_EXIT,"User Proactively Exit"));
        httpSession.invalidate();
        httpServletRequest.getSession(true);
        log.info(httpSession.getId());
    }

    @RequestMapping(value = "/countonlineuser",method = RequestMethod.GET)
    public String countonlineuser(HttpSession httpSession){
        log.info("httpsession"+httpSession.getId());
        redisTemplate.getClientList();
        redisTemplate.opsForValue().set("countUserNum", WebSocketConnect.onlineusernum());
        return (Objects.requireNonNull(redisTemplate.opsForValue().get("countUserNum"))).toString();
    }
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public Object test(@RequestBody String uid){
        log.info(uid);
        return historyMessageService.load(uid);
    }
    @RequestMapping(value = "/searchuser",method = RequestMethod.POST)
    public String  searchuser(@RequestBody ObjectNode searchData,HttpSession httpSession)   {
        try{
            User user = (User)httpSession.getAttribute("user");
            if (userService.isOnline(user.getUID())) {
                if (searchData.get("userName").asText().length() <=0) {
                    searchData.put("userName", "^");
                }
                Object data = userService.searchUser(searchData.get("userName").asText());
                if (data.getClass().getSimpleName().equals("String")) {
                    return (String) data;
                } else {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsondata = objectMapper.valueToTree(data);
                    return jsondata.toString();
                }
            } else {
                return "user invalid";
            }
        }catch (Exception e){
            return "user invalid";
        }


    }

    @RequestMapping(value = "/addFriend",method = RequestMethod.POST)
    public String addFriend(@RequestBody String uid,HttpSession httpSession) throws EncodeException, IOException {
        if (userService.isOnline(httpSession)){
            return (String) userService.addFriends(UserUtil.getHttpSessionUser(httpSession),uid);
        }else{
            return "null";
        }
    }

    @RequestMapping(value="/getfriendlist",method = RequestMethod.POST)
    public String getfriendlist(@NotNull HttpSession httpSession) {
        if (userService.isOnline(httpSession)){
            Object dataobject = userService.getFriends((User) httpSession.getAttribute("user"));
            System.out.println(dataobject.getClass().toString());
            if (dataobject.getClass().getSimpleName().equals("ArrayNode")){
                return dataobject.toString();
            }
        }
        return "null";
    }

    @RequestMapping(value = "/getAddFriendRequest",method = RequestMethod.POST)
    public Object getaddfriendrequest(HttpSession httpSession) {
        if(userService.isOnline(httpSession)){
            Set<ObjectNode> data =  postSerivce.GetAddFriendRequest(UserUtil.getHttpSessionUser(httpSession).getUID());
            if (data.isEmpty()){
                return new ArrayList<>();
            }else{
                return data;
            }
        }else{
            return "null";
        }
    }

    @RequestMapping(value = "/getUserName")
    public String getUserName(String uid,HttpSession httpSession){
        if(userService.isOnline(httpSession)){
            return  userService.getUserName(uid);
        }else{
            return null;
        }
    }

    @RequestMapping(value = "/pullHistoryMessage",method = RequestMethod.POST)
    public Object pullHistoryMessage(HttpSession httpSession){
        if(userService.isOnline(httpSession)){
            Map<String,List<Message>> data = historyMessageService.load(UserUtil.getHttpSessionUser(httpSession).getUID());
            log.info(String.valueOf(data.size()), objectMapper.valueToTree(data).asText());
//            return objectMapper.valueToTree(data).asText();
            return data;
        }
        return null;

    }

    public Object getHistoryMessage(){
        return null;
    }

    public Object getHistoryMessageSummary(){
        return  null;
    }
}