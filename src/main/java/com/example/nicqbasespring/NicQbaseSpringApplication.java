package com.example.nicqbasespring;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
public class NicQbaseSpringApplication {

    public static void main(String[] args) {
       SpringApplication.run(NicQbaseSpringApplication.class, args);
    }
}
