package com.example.nicqbasespring.util;

import com.example.nicqbasespring.entries.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {
    public static JsonNode getUserJson(User user){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.valueToTree(user);
    }
    public static Boolean phoneNumberChecker(String phoneNumberChecker){
        return false;
    }
}

