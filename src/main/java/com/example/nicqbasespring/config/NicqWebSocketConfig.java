package com.example.nicqbasespring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Map;
// webscoket 配置，提供httpsession 的转置
//
@Configuration
@Slf4j
public class NicqWebSocketConfig extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake( ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession  httpSession  = (HttpSession) request.getHttpSession();
        Map<String,Object> userProperties = sec.getUserProperties();
        try{
            userProperties.put(HttpSession.class.getName(),httpSession);
        }catch (NullPointerException exception){
            log.info("Unknow Error");
        }
        super.modifyHandshake(sec, request, response);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}