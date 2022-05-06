package com.example.nicqbasespring.service;

import com.example.nicqbasespring.dao.MessageDao;
import com.example.nicqbasespring.dao.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class AbstraService {
    protected static UserDao userDao;
    protected static MessageDao messageDao;
    protected static ObjectMapper objectMapper;
    protected static RedisTemplate<String,Object> redisTemplate;
    @Autowired(required = false)
    public void setUserDao(UserDao userDao) {
        AbstraService.userDao = userDao;
    }
    @Autowired(required = false)
    public void setMessageDao(MessageDao messageDao){
        AbstraService.messageDao = messageDao;
    }
    @Autowired(required = false)
    public void setObjectMapper(ObjectMapper objectMapper) {
        AbstraService.objectMapper = objectMapper;
    }

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        AbstraService.redisTemplate = redisTemplate;
    }
}
