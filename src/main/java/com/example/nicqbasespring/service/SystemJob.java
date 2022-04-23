package com.example.nicqbasespring.service;

import com.example.nicqbasespring.controller.WebSocketConnect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.websocket.EncodeException;
import java.io.IOException;

@Service
@EnableScheduling
@Slf4j
public class SystemJob {
    @Scheduled(fixedDelay =10000)
    public void run() throws EncodeException, IOException {
        WebSocketConnect.KeepHeart();
    }
}
