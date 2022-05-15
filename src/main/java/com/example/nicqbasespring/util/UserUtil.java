package com.example.nicqbasespring.util;

import com.example.nicqbasespring.entries.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
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
        List<T> copy = new ArrayList<>();
        while(iterator.hasNext()){
            copy.add(iterator.next());
        }
        return copy;
    }
    public static User getHttpSessionUser(HttpSession httpSession){
        return (User) httpSession.getAttribute("user");
    }
    public static <T extends Enum<T>> T getEnumFromString(Class<T> clazz,String desc){
        if(clazz !=null && desc!=null){
            try{
                return Enum.valueOf(clazz,desc.trim());

            }catch (IllegalStateException illegalStateException){

            }
        }
        return null;
    }
}

