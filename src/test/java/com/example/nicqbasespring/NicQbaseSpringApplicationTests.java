package com.example.nicqbasespring;

import com.example.nicqbasespring.config.NicqBaseConfiguration;
import com.example.nicqbasespring.dao.UserDao;
import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.service.UserService;
import com.example.nicqbasespring.util.UserUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

@SpringBootTest
class NicQbaseSpringApplicationTests {

    static ApplicationContext context;
    static UserService userService;
    static UserDao userDao;
    @Test
    void contextLoads() throws IOException, InterruptedException {
       context =  new AnnotationConfigApplicationContext(NicqBaseConfiguration.class);
       userService = context.getBean("userService",UserService.class);
       userDao = context.getBean("userDao",UserDao.class);
       test3 ();
       test5();
    }
    /*
        Object User Test!
     */
    public static void test1(){
        System.out.println("yes");
        User user = context.getBean("user",User.class);
        System.out.println(user.getPermission());
    }
    public static void test2(){
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
    public static  void test3(){
        User user = (User) userDao.getUser((String) userDao.getUid("Rider"));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("李重光")));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("mk3")));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("awq")));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("阿巴瑟")));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("Rider")));
    }
    public static void test4(){
        User user = (User) userDao.getUser((String) userDao.getUid("Rider"));
        System.out.println(userService.removeFriend(user,(String) userDao.getUid("李重光")));
        System.out.println(
                userService.removeFriend(
                        user,
                        (String) userDao.getUid("影")
                )
        );
    }
    public static void test5() throws JsonProcessingException {
        User user  = (User) userDao.getUser((String) userDao.getUid("Rider"));
        System.out.println(userService.getFriends(user));
    }

}
