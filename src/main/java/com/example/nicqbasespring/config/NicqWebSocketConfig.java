package com.example.nicqbasespring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Map;
// webscoket 配置，提供httpsession 的转置
//
public class NicqWebSocketConfig extends ServerEndpointConfig.Configurator {
    private final Logger log = LoggerFactory.getLogger(NicqWebSocketConfig.class);
    @Override
    public void modifyHandshake( ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession  httpSession  = (HttpSession) request.getHttpSession();
        Map<String,Object> userProperties = sec.getUserProperties();
        try{
            userProperties.put(HttpSession.class.getName(),httpSession);
        }catch (NullPointerException exception){
            exception.printStackTrace();
        }
        super.modifyHandshake(sec, request, response);
    }
}