package com.example.nicqbasespring.dao;

import clojure.lang.Obj;
import com.example.nicqbasespring.entries.DataType;
import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.entries.MessageType;
import com.example.nicqbasespring.exception.MessageErrorType;
import com.example.nicqbasespring.exception.MessageException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.nicqbasespring.util.UserUtil.getEnumFromString;

@Repository
@Slf4j
public class MessageDao extends AbstraDao implements MessageDaoImpl{
    public void addFriendRequest(Message message){
        if( ! checkrepeat(message)){
            redisTemplate.opsForSet().add(message.getTo() + "AddFriendRequestList", message);
        }else{
            throw  new MessageException(MessageErrorType.Repeat_Post);
        }
    }
    public Set<Object> getFriednRequest(String uid){
        return redisTemplate
                .opsForSet()
                .members(uid+"AddFriendRequestList");
    }
    public void send(@NotNull Message message){
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
    public void accept(@NotNull Message message){
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
    public List<Message> loadHistoryMessage(String uid, String fid, int base, int offset){
        String inbox = uid+"_inbox";
        String outbox = uid+"_outbox";
        String sql = String.format(
                "SELECT  * FROM(select * FROM nicqmessagedatabase.`%s` where fromuser =? union select * from nicqmessagedatabase.`%s` where  touser =?) as data order by unixtime limit ?,?",
                inbox,outbox
        );
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Message(
                (String)rs.getObject("fromuser")
                ,(String) rs.getObject("touser")
                ,rs.getObject("data")
                ,rs.getTimestamp("unixtime")
                ,getEnumFromString(DataType.class, (String) rs.getObject("dataType"))
                , getEnumFromString(MessageType.class,(String) rs.getObject("messageType"))
        ), fid, fid, base, offset);
    }
    public Integer getHistoryMessageCount(String uid,String fid){
        String inbox = uid+"_inbox";
        String sql = String.format("select count(*) from nicqmessagedatabase.`%s` inbox where inbox.fromuser =?",inbox);
        return  jdbcTemplate.queryForObject(sql,Integer.class,fid);
    }
    public Boolean checkrepeat(@NotNull Message message){
        Set<Object> messages = redisTemplate.opsForSet().members(message.getTo()+"AddFriendRequestList");
        if(messages!=null) {
            for (Object object : messages) {
                if (((Message) object).getData().equals(message.getData())) {
                    return true;
                }
            }
        }
        return false;
    }
    public void cleanaddFriendRequestlist(String uid,String fid){
        Set<Object> data =  redisTemplate.opsForSet().members(uid+"AddFriendRequestList");
        if(data!=null) {
            for (Object object : data) {
                if (((Message) object).getFrom().equals(fid)) {
                    redisTemplate.opsForSet().remove(uid + "AddFriendRequestList", object);
                }
            }
        }
    }
}
