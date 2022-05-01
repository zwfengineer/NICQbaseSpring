package com.example.nicqbasespring;
import com.example.nicqbasespring.dao.MessageDao;
import com.example.nicqbasespring.dao.UserDao;
import com.example.nicqbasespring.entries.DataType;
import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.entries.MessageType;
import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.service.UserService;
import com.example.nicqbasespring.util.UserUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
    @Autowired
    MessageDao messageDao;

    @Test()
    void contextLoads() throws JsonProcessingException {

    }
    /*
        Object User Test!
     */
    @Test
    public  void test1(){
        System.out.println("yes");
        User user = context.getBean("user",User.class);
        System.out.println(user.getPermission());
    }
    @Test
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
    @Test
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
    @Test
    public void test3(){
        User user = (User) userDao.getUser((String) userDao.getUid("Rider"));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("李重光")));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("mk3")));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("awq")));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("阿巴瑟")));
        System.out.println(userService.addFriends(user,(String) userDao.getUid("Rider")));
    }
    @Test
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
    @Test
    public void test7() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonstring = "{\"userName\":\"Rider\"}";
        JsonNode json = objectMapper.readTree(jsonstring);
        List<Map<String, Object>> data = userDao.searchUser(json.get("userName").asText());
        JsonNode jsondata = objectMapper.valueToTree(data);
        System.out.println(jsondata.toString());
        System.out.println(data.size());
    }

    @Test
    public void test8(){
        String uid = "LL";
        userDao.createinbox(uid);
        userDao.createoutbox(uid);
        JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("nicqjdbcTemplate");
        String sql = String.format("drop table nicqmessagedatabase.`%s`",uid);
        jdbcTemplate.update(sql);

    }
    @Test
    @Transactional
    public void test10(){
        String uid = "22-04-29-10";
        String fid = "22-04-29-11";
        for(int i = 0;i<=10;i++){
            messageDao.send(new Message(uid, fid, ((i*2)),
                    new Timestamp(System.currentTimeMillis()),DataType.Text,MessageType.UserMessage
                    ));
            messageDao.accept(new Message(fid, uid, ((i*2)+1),
                    new Timestamp(System.currentTimeMillis()), DataType.Text, MessageType.UserMessage
            ));
        }
        System.out.println(messageDao.getHistoryMessageCount("22-04-29-10","22-04-29-11"));
    }

}
