package com.example.nicqbasespring;

import com.example.nicqbasespring.config.NicqBaseConfiguration;
import com.example.nicqbasespring.dao.UserDao;
import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.service.UserService;
import com.example.nicqbasespring.util.UserUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
class NicQbaseSpringApplicationTests {

    static ApplicationContext context;
    static UserService userService;
    static UserDao userDao;
    @Test
    void contextLoads() {
       context =  new AnnotationConfigApplicationContext(NicqBaseConfiguration.class);
       userService = context.getBean("userService",UserService.class);
       userDao = context.getBean("userDao",UserDao.class);
    }

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
    public static  void test3(){
        User user = (User) userDao.getUser((String) userDao.getUid("Rider"));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("李重光")));
    }
    public static void test4(){
        User user = (User) userDao.getUser((String) userDao.getUid("Rider"));
        userService.removeFriend(user,(String) userDao.getUid("李重光"));
        userService.removeFriend(user,(String) userDao.getUid("影"));
    }

}
