package com.example.nicqbasespring.config;

import com.example.nicqbasespring.controller.WebSocketConnect;
import com.example.nicqbasespring.exception.WebSocketCloseCode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.websocket.CloseReason;
import java.io.IOException;

@Component
@Slf4j
public class NicqStopEventListener implements ApplicationListener<ContextClosedEvent> {
    @Autowired
    RedisTemplate <String, Object> redisTemplate;
    @Override
    public void onApplicationEvent(@NotNull ContextClosedEvent event) {
        redisTemplate.delete("onlineuser");
        try {
            WebSocketConnect.ServiceShutdown(new CloseReason(WebSocketCloseCode.Serve_Shutdown_Proactively,"Server shutdown proactvely"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
