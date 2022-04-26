package com.example.nicqbasespring.dao;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstraDao {
    protected JdbcTemplate jdbcTemplate;
    protected RedisTemplate<String,Object> redisTemplate;
}
