package com.example.nicqbasespring;

import clojure.lang.Obj;
import com.example.nicqbasespring.config.*;
import com.example.nicqbasespring.dao.UserDao;
import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.service.UserService;
import com.example.nicqbasespring.util.UserUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class NicQbaseSpringApplicationTests {
    @Autowired
    ApplicationContext context;
    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Test()
    void contextLoads() throws JsonProcessingException {
        test7();
    }
    /*
        Object User Test!
     */
    public  void test1(){
        System.out.println("yes");
        User user = context.getBean("user",User.class);
        System.out.println(user.getPermission());
    }
    public  void test2(){
        User user = context.getBean("user",User.class);
        user.setPermission("global");
        user.setUserName("lili");
        user.setPasswd(null);
        System.out.println(UserUtil.getUserJson(user)) ;
    }
    /*
    UserDao+UserService 测试

    功能测试：添加与删除好友
     */
    public void test3(){
        User user = (User) userDao.getUser((String) userDao.getUid("Rider"));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("李重光")));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("mk3")));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("awq")));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("阿巴瑟")));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("Rider")));
    }
    public void test4(){
        User user = (User) userDao.getUser((String) userDao.getUid("Rider"));
        System.out.println(userService.removeFriend(user,(String) userDao.getUid("李重光")));
        System.out.println(
                userService.removeFriend(
                        user,
                        (String) userDao.getUid("影")
                )
        );
    }
    public void test5() {
        User user  = (User) userDao.getUser((String) userDao.getUid("Rider"));
        System.out.println(userService.getFriends(user));
    }
    @Test
    public void test6(){
        String uid = "114514";

        String  sessionid = "117517";
        HashMap<String,String> map = new HashMap<>();
        map.put(sessionid,uid);
        map.put(sessionid+1,uid+1);
        redisTemplate.opsForHash().putAll("onlineuser",map);
        System.out.println(redisTemplate.opsForHash().get("onlineuser",uid));
        System.out.println(redisTemplate.opsForHash().values("onlineuser"));

    }

    public void test7() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonstring = "{\"userName\":\"Rider\"}";
        JsonNode json = objectMapper.readTree(jsonstring);
        List<Map<String, Object>> data = userDao.searchUser(json.get("userName").asText());
        JsonNode jsondata = objectMapper.valueToTree(data);
        System.out.println(jsondata.toString());
        System.out.println(data.size());
    }
}
