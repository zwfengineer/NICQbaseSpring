package com.example.nicqbasespring.util;

import com.example.nicqbasespring.entries.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.lang.module.Configuration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class UserUtil {
    public static JsonNode getUserJson(User user){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.valueToTree(user);
    }
    public static Boolean phoneNumberChecker(String phoneNumberChecker){
        return false;
    }
    public static <T>List<T> asList(Iterator<T> iterator){
        List<T> copy = new ArrayList<T>();
        while(iterator.hasNext()){
            copy.add(iterator.next());
        }
        return copy;
    }
}

