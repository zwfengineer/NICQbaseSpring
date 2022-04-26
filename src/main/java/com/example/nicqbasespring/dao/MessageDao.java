package com.example.nicqbasespring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageDao extends AbstraDao implements MessageDaoImpl{

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public Object addFriendRequest(String uid,String fid){
        String Sql  = "";
        return null;
    }
}
