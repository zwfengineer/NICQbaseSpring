package com.example.nicqbasespring.dao;

import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.entries.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Repository
public class UserDao implements UserDaoImpl{
    private Logger log;
    private JdbcTemplate jdbctemplate;
    @Autowired(required = false)
    public void setJdbctemplate(JdbcTemplate jdbctemplate) {
        this.jdbctemplate = jdbctemplate;
    }

    @Autowired(required = false)
    public void setLog(Logger log) {
        this.log = log;
    }

    @Override
    public Object getUserName(String uid) {
        String sql = "select username from user where uid=?";
        return jdbctemplate.queryForObject(sql,String.class,uid);
    }

    @Override
    public Object checkUserNum(@NotNull User user) {
        String sql = "select count(*) from user where uid=? and passwd=? ";
        String uid = user.getUID();
        String passwd = user.getPasswd();
        return jdbctemplate.queryForObject(sql,Integer.class, uid,passwd);
    }
    @Override
    public Object checkUserNum(@NotNull String uid){
        String sql = "select count(*) from user where uid=?";
        return jdbctemplate.queryForObject(sql,Integer.class, uid);
    }

    @Override
    public Object getAllUserNum() {
        String sql = "select count(*) from user";
        return jdbctemplate.queryForObject(sql,Integer.class);
    }

    @Override
    public Object modifyUserLevel(User user) {
        return null;
    }

    @Override
    public Object modifyUserStatus(User user) {
        return null;
    }

    @Override
    public Object modifyUserInfo(User user) {
        return null;
    }

    @Override
    public Object ConnectionUser(User user, String uid) {
        return null;
    }

    @Override
    public Object getUid(String username) {
        String sql="select uid from user where username = ?";
        try{
            return jdbctemplate.queryForObject(sql,String.class,username);
        }catch (Exception e){
            log.info(String.valueOf(e.getClass()));
            return "查无此人！！！！";
        }

    }

    @Override
    public Object addUser(User user) {
        String sql = "insert into user values(?,?,?,?,NOW(),?,?)";
        Object[] args = {
                user.getUserName(),
                user.getUID(),
                user.getPermission(),
                user.getPasswd(),
                user.getGender(),
                user.getAvatar(),
        };
        log.info(user.toString());
        return jdbctemplate.update(sql,args);
    }

    @Override
    public Object getUserNum(String username) {
        String sql = "select count(*) from user where username = ?";
        return jdbctemplate.queryForObject(sql,Integer.class,username);
    }

    @Override
    public Object putHistoryMessage(String uid, Message message) {  
        return null;
    }

    @Override
    public Object  getUser(String uid) {
        /*
        you must be check this uid is real
         */
        if ((Integer) checkUserNum(uid)==1){
            String sql = "select * from user where uid=?";
            return  jdbctemplate.queryForObject(sql,new BeanPropertyRowMapper<>(User.class),uid);
        }else {
            return "ERR:No User";
        }

   }

    @Override
    public void addFriend(String uid,String fid) {
        String sql = "insert into friends values(?,?,NOW())";
        jdbctemplate.update(sql,uid,fid);
        jdbctemplate.update(sql,fid,uid);
    }
    public Object addFriend(Map<String,String> friends){
        return null;
    }
    public Object  checkFriend(String uid,String fid){
        String sql = "select count(*) from friends where uid = ? and fid=?";
        return jdbctemplate.queryForObject(sql,Integer.class,uid,fid);
    }
    public Object checkFriend(String uid){
        String sql = "select count(*) from friends where uid = ?";
        return jdbctemplate.queryForObject(sql,Integer.class,uid);
    }

    public void removeFriend(String uid,String fid){
        String sql = "DELETE FROM friends where uid=? and fid=?";
        jdbctemplate.update(sql,uid,fid);
        jdbctemplate.update(sql,fid,uid);
    }

    public List<String> getFriends(String uid){
        String sql="select fid from friends where uid=?";
        return jdbctemplate.query(sql,new BeanPropertyRowMapper<>(String.class),uid);
    }
}
