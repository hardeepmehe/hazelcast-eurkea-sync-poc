package com.htec.user.userservice;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@CacheConfig
public class UserService {

    Map<String, String> mockDB = new HashMap<>();

    UserService() {
        for (int i = 0; i < 10; i++)
            mockDB.put(String.valueOf(i), String.valueOf(i));
    }

    @Cacheable(cacheNames = "users", key = "#userId")
    public String getUserNameById(String userId) {
        System.out.println("searching username in db by id" + userId);

        return mockDB.get(userId);
    }

    @CachePut(cacheNames = "users", key = "#userId")
    public String setUserName(String userName, String userId) {
        System.out.println("setting userName" + userName + " for id" + userId);
        mockDB.put(userId, userName);
        return mockDB.get(userId);
    }

}
