package com.example.nicqbasespring.dao;

import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.exception.MessageErrorType;
import com.example.nicqbasespring.exception.MessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageDao extends AbstraDao implements MessageDaoImpl{

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public Boolean addFriendRequest(Message message){
        if(Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(message.getTo() + "AddFriendRequestList", message))){
            try {
                redisTemplate
                        .opsForSet().add(message.getTo() + "AddFriendRequestList", message);
            }catch (Exception e){
                return false;
            }
            return true;
        }else{
            throw new MessageException(MessageErrorType.Repeat_Post);
        }
    }
    public Object getFriednRequest(String uid){
        return redisTemplate
                .opsForSet()
                .members(uid+"AddFriendRequestList");
    }
}
