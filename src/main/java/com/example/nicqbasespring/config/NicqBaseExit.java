package com.example.nicqbasespring.config;

import clojure.lang.Obj;
import com.example.nicqbasespring.controller.WebSocketConnect;
import com.example.nicqbasespring.exception.WebSocketCloseCode;
import lombok.Data;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.websocket.CloseReason;
import java.io.IOException;

@Component
public class NicqBaseExit {
    @Autowired
    RedisTemplate <String, Object> redisTemplate;
    @PreDestroy
    public void destroy() throws IOException {
        WebSocketConnect.ServiceShutdown(new CloseReason(WebSocketCloseCode.USER_REPEAT_CONNECT,"Serve_Shutdown_Proactively"));
        redisTemplate.delete("onlineuser");
    }
}
