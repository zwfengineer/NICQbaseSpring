package com.example.nicqbasespring;

import com.example.nicqbasespring.config.NicqBaseConfiguration;
import com.example.nicqbasespring.dao.UserDao;
import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.service.UserService;
import com.example.nicqbasespring.util.UserUtil;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.logging.Logger;

@SpringBootApplication
public class NicQbaseSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(NicQbaseSpringApplication.class, args);
    }
}
