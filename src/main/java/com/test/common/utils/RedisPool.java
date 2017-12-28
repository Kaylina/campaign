package com.test.common.utils;

import com.alibaba.fastjson.JSON;
import com.test.common.constant.ApplicationProperties;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by lemon on 2017/7/28.
 */
public class RedisPool {

    public static JedisPool jedisPool; // jedis连接池

    static {

        String ip = ApplicationProperties.getString("redis.ip");
        int port = Integer.parseInt(ApplicationProperties.getString("redis.port"));
        JedisPoolConfig config = new JedisPoolConfig();
        //设置最大连接数
        config.setMaxTotal(Integer.valueOf(ApplicationProperties.getString("redis.maxTotal")));
        //设置最大空闲数
        config.setMaxIdle(Integer.valueOf(ApplicationProperties.getString("redis.maxIdle")));
        //设置超时时间
        config.setMaxWaitMillis(Long.valueOf(ApplicationProperties.getString("redis.maxWaitTime")));
        //初始化连接池
        jedisPool = new JedisPool(config, ip, port);
    }

    /**
     * Set 'String' to redis
     *
     * @param key   key
     * @param value value
     * @return
     * @throws Exception
     */
    public static boolean set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * Set 'Object' to redis
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean set(String key, Object value) {
        Jedis jedis = null;
        try {
            String objectJson = JSON.toJSONString(value);
            jedis = jedisPool.getResource();
            jedis.set(key, objectJson);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * del value by key from redis
     *
     * @param key
     * @return
     */
    public static boolean del(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * get value by key
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Object value = jedis.get(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }


    /**
     * get value by key from redis
     *
     * @param key
     * @return
     */
    public static <T> T get(String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            return JSON.parseObject(value, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public static boolean setExpire(String key, String value, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key, seconds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

}