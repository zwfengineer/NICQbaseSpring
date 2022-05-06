package com.example.nicqbasespring.dao;

import clojure.lang.Obj;
import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.exception.MessageErrorType;
import com.example.nicqbasespring.exception.MessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@Slf4j
public class MessageDao extends AbstraDao implements MessageDaoImpl{
    public void addFriendRequest(Message message){
        if((Boolean.FALSE.equals(redisTemplate.opsForSet().isMember(message.getTo() + "AddFriendRequestList", message)))){
                redisTemplate
                        .opsForSet().add(message.getTo() + "AddFriendRequestList", message);
        }else{
            throw new MessageException(MessageErrorType.Repeat_Post);
        }
    }
    public Set<Object> getFriednRequest(String uid){
        return redisTemplate
                .opsForSet()
                .members(uid+"AddFriendRequestList");
    }
    public void send(Message message){
        String tablename = message.getFrom() +"_outbox";
        String sql =  String.format("insert into nicqmessagedatabase.`%s` values(?,?,?,?,?,?)",tablename);
        Object[] args =new Object[]{
                message.getFrom(),
                message.getTo(),
                message.getData(),
                message.getUnixTime(),
                message.getDataType().toString(),
                message.getMessageType().toString()
        };
        jdbcTemplate.update(sql,args);
    }
    public void accept(Message message){
        String tablename = message.getTo()+"_inbox";
        String sql = String.format("insert into nicqmessagedatabase.`%s` values(?,?,?,?,?,?)",tablename);
        Object[] args =new Object[]{
                message.getFrom(),
                message.getTo(),
                message.getData(),
                message.getUnixTime(),
                message.getDataType().toString(),
                message.getMessageType().toString()
        };
        jdbcTemplate.update(sql,args);
    }
    public Message[] loadHistoryMessage(String uid,String fid,int base,int offset){
        String inbox = uid+"_inbox";
        String outbox = uid+"_outbox";
        String sql = String.format(
                "SELECT  * FROM(select * from `%s` where fromuser =? union select * from `%s` where  touser =?) as data order by unixtime limit ?,?",
                inbox,outbox
        );
        jdbcTemplate.queryForList(sql,fid,fid,base,offset);
        return new Message[0];
    }
    public Integer getHistoryMessageCount(String uid,String fid){
        String inbox = uid+"_inbox";
        String sql = String.format("select count(*) from nicqmessagedatabase.`%s` inbox where inbox.fromuser =?",inbox);
        return  jdbcTemplate.queryForObject(sql,Integer.class,fid);
    }
}
