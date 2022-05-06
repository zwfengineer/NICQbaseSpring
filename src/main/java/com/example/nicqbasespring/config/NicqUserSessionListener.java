package com.example.nicqbasespring.config;

import com.example.nicqbasespring.controller.WebSocketConnect;
import com.example.nicqbasespring.exception.WebSocketCloseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.websocket.CloseReason;
import java.io.IOException;

@WebListener
@Configuration
@Slf4j
public class NicqUserSessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.info("sessionCreated:::"+se.getSession().getId());
        se.getSession().setMaxInactiveInterval(1);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        log.info("Session Destory::"+se.getSession().getId());
        try {
            WebSocketConnect.logout(
                    se.getSession(),
                    new CloseReason(WebSocketCloseCode.USER_SESSION_TIMEOUT,"User_Session_Timeout"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
