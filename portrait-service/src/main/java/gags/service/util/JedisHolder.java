package gags.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyl on 2017/3/25.
 */
@Component
public class JedisHolder {

    @Value("${redisInfos}")
    private String redisInfos;

    private static final int JedisClientTimeOutMills = 600000;
    private static final int MAX_IDLE = 100;
    private static final int MAX_TOTAL = 2000;

    ShardedJedisPool shardPool;



    @PostConstruct
    public void init(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        // jedisPoolConfig.setMaxActive(5000);
        jedisPoolConfig.setMaxIdle(MAX_IDLE);
        jedisPoolConfig.setMaxTotal(MAX_TOTAL);
        jedisPoolConfig.setTestOnReturn(false);
        // jedisPoolConfig.setMaxWait(10000);

        jedisPoolConfig.setTestOnBorrow(false);

        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        String[] arr = redisInfos.split(",");
        for (String ipPort : arr)
        {
            String[] carr = ipPort.split(":");
            JedisShardInfo info=new JedisShardInfo(carr[0],Integer.valueOf(carr[1]),JedisClientTimeOutMills);
            if(carr.length==3){
                info.setPassword(carr[2]);
            }
            shards.add(info);
        }

        shardPool = new ShardedJedisPool(jedisPoolConfig, shards);
    }

    public ShardedJedisPool getShardedJedisPool(){
        return shardPool;
    }
}
