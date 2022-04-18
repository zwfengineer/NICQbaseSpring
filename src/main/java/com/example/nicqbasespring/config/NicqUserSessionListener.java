package com.example.nicqbasespring.config;

import com.example.nicqbasespring.controller.WebSocketConnect;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.IOException;

@WebListener
public class NicqUserSessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        try {
            WebSocketConnect.logout(se.getSession());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
