package com.example.nicqbasespring;
import clojure.lang.Obj;
import com.example.nicqbasespring.dao.MessageDao;
import com.example.nicqbasespring.dao.UserDao;
import com.example.nicqbasespring.entries.DataType;
import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.entries.MessageType;
import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.service.HistoryMessageService;
import com.example.nicqbasespring.service.UserService;
import com.example.nicqbasespring.util.UserUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
class NicQbaseSpringApplicationTests {
    @Autowired
    HistoryMessageService historyMessageService;
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
    void contextLoads() {

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
    public void test4() {
        User user = (User) userDao.getUser(userDao.getUid("Rider"));
        System.out.println(userService.removeFriend(user, userDao.getUid("李重光")));
        System.out.println(
                userService.removeFriend(
                        user,
                        userDao.getUid("影")
                )
        );
    }
    @Test
    public void test3() {
        User user = (User) userDao.getUser(userDao.getUid("Rider"));
        System.out.println(userService.addFriends(user, userDao.getUid("李重光")));
        System.out.println(userService.addFriends(user, userDao.getUid("mk3")));
        System.out.println(userService.addFriends(user, userDao.getUid("awq")));
        System.out.println(userService.addFriends(user, userDao.getUid("阿巴瑟")));
        System.out.println(userService.addFriends(user, userDao.getUid("Rider")));
    }
    @Test
    public void test5() {
        User user  = (User) userDao.getUser(userDao.getUid("Rider"));
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
        String[] tablenames = new String[]{uid+"_inbox",uid+"_outbox"};
        for(String tablename:tablenames){
            jdbcTemplate.update(String.format("drop table nicqmessagedatabase.`%s`",tablename));
        }
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

    @Test
    public void test11(){
        Message message = new Message("bb","aa",DataType.Text,new Timestamp(System.currentTimeMillis()),MessageType.UserMessage);
        System.out.println(message);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.valueToTree(message);
        System.out.println(objectNode);
    }
    @Test
    public void test12(){
        try {
            log.info(userService.getUserName("lll"));
        }catch (EmptyResultDataAccessException exception){
            log.info("null");
        }
    }
    @Test
//    MessageService->loadhistoy
    public void test13(){
        System.out.println(historyMessageService.load("22-05-14-11"));
    }
    @Test
    public void test14(){
//        Random random = new Random();
//        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
//        List<String> ids = jdbcTemplate.queryForList("select uid from user",String.class);
//        for(String id:ids){
//            String sql ="update user set avatar=? where uid=?";
//
//            jdbcTemplate.update(sql,random.nextInt(12),id);
//        }
    }

    @Test
    public void test15(){
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        List<Map<String, Object>> data = jdbcTemplate.queryForList("select uid,passwd from user");
        for(Map<String, Object> item:data){
            System.out.println(item.get("uid").toString()+item.get("passwd"));
//            jdbcTemplate.update(
//                    "update user set passwd=? where uid=?",
//                    UserUtil.encipher((String) item.get("passwd")),
//                    item.get("uid")
//            );
        }
    }
    @Test
    public void test16(){
        String passwd = "125788";
        String bcrpasswd = UserUtil.encipher(passwd);
        log.info(bcrpasswd);
        Boolean data = UserUtil.checkpasswd(passwd,bcrpasswd);
        log.info(String.valueOf(data));
    }

}
