package gags.service.util;

/**
 * Created by zhangt on 2017/11/22.
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.SortingParams;


public class RedisClient {
    
    private String redisIP = "10.2.22.18";
    private int redisPort = 6378;

    public static Jedis jedis = null;//非切片额客户端连接
    private JedisPool jedisPool;//非切片连接池
    public static ShardedJedis shardedJedis = null;//切片额客户端连接
    private ShardedJedisPool shardedJedisPool;//切片连接池

    public RedisClient() {
        initialPool();
        initialShardedPool();
        shardedJedis = shardedJedisPool.getResource();
        jedis = jedisPool.getResource();


    }

    public static Jedis getJedis(){
        if(jedis == null){
            new RedisClient();
        }
        return jedis;
    }

    public static ShardedJedis getShardedJedis(){
        if(shardedJedis == null){
            new RedisClient();
        }
        return shardedJedis;
    }

    /**
     * 初始化非切片池
     */
    private void initialPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(10000l);
        config.setTestOnBorrow(false);

        jedisPool = new JedisPool(config, redisIP, redisPort);
    }

    /**
     * 初始化切片池
     */
    private void initialShardedPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(10000l);
        config.setTestOnBorrow(false);
        // slave链接
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo(redisIP, redisPort, "master"));

        // 构造池
        shardedJedisPool = new ShardedJedisPool(config, shards);
    }

    public void show() {
        System.out.println("获取hashs中所有的key："+shardedJedis.hkeys("1511317610003").size());
        System.out.println("获取hashs中所有的value："+shardedJedis.hvals("1511317610003"));
        jedisPool.returnResource(jedis);
        shardedJedisPool.returnResource(shardedJedis);
    }
}
