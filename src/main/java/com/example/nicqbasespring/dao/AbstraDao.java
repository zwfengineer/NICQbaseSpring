package com.example.nicqbasespring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstraDao {
    protected JdbcTemplate jdbcTemplate;
    protected RedisTemplate<String,Object> redisTemplate;
    @Autowired(required = false)
    public void setJdbctemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
