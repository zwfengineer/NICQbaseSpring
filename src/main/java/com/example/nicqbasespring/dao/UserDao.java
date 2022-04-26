package com.example.nicqbasespring.dao;

import com.example.nicqbasespring.entries.User;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserDao extends AbstraDao implements UserDaoImpl{

    @Autowired(required = false)
    public void setJdbctemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public Object getUserName(String uid) {
        String sql = "select username from user where uid=?";
        return jdbcTemplate.queryForObject(sql,String.class,uid);
    }

    @Override
    public Object checkUserNum(@NotNull User user) {
        String sql = "select count(*) from user where uid=? and passwd=? ";
        String uid = user.getUID();
        String passwd = user.getPasswd();
        return jdbcTemplate.queryForObject(sql,Integer.class, uid,passwd);
    }
    @Override
    public Object checkUserNum(@NotNull String uid){
        String sql = "select count(*) from user where uid=?";
        return jdbcTemplate.queryForObject(sql,Integer.class, uid);
    }

    @Override
    public Object getAllUserNum() {
        String sql = "select count(*) from user";
        return jdbcTemplate.queryForObject(sql,Integer.class);
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
    public Object getUid(String username) {
        String sql="select uid from user where username = ?";
        try{
            return jdbcTemplate.queryForObject(sql,String.class,username);
        }catch (Exception e){
            log.info(String.valueOf(e.getClass()));
            return "查无此人！！！！";
        }

    }

    @Override
    public Object addUser(@NotNull User user) {
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
        return jdbcTemplate.update(sql,args);
    }

    @Override
    public Object getUserNum(String username) {
        String sql = "select count(*) from user where username = ?";
        return jdbcTemplate.queryForObject(sql,Integer.class,username);
    }

    @Override
    public Object  getUser(String uid) {
        /*
        you must be check this uid is real
         */
        if ((Integer) checkUserNum(uid)==1){
            String sql = "select * from user where uid=?";
            return  jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(User.class),uid);
        }else {
            return "ERR:No User";
        }

   }

    @Override
    public void addFriend(String uid,String fid) {
        String sql = "insert into friends values(?,?,NOW())";
        jdbcTemplate.update(sql,uid,fid);
        jdbcTemplate.update(sql,fid,uid);
    }


    public Object checkFriend(String uid,String fid){
        String sql = "select count(*) from friends where uid = ? and fid=?";
        return jdbcTemplate.queryForObject(sql,Integer.class,uid,fid);
    }

    public Object checkFriend(String uid){
        String sql = "select count(*) from friends where uid = ?";
        return jdbcTemplate.queryForObject(sql,Integer.class,uid);
    }

    public void removeFriend(String uid,String fid){
        String sql = "DELETE FROM friends where uid=? and fid=?";
        jdbcTemplate.update(sql,uid,fid);
        jdbcTemplate.update(sql,fid,uid);
    }

    public List<Map<String,Object>> getFriends(String uid){
        String sql="select fid,user.username from friends,user where friends.uid=? and user.uid = friends.fid";
        return jdbcTemplate.queryForList(sql,uid);
    }

    @Override
    public List<Map<String, Object>> searchUser(String username) {
        String sql = "select uid,username,gender from user where uid regexp ? or username regexp ?";
        log.info(username);
        return jdbcTemplate.queryForList(sql,username,username);
    }

    @Override
    public List<Map<String,Object>> searchUser(String uid, String gender) {
        String sql = "select uid,username from user where uid like ? or username like ? and gender = ?";
        return jdbcTemplate.queryForList(sql,uid,gender);
    }
}
