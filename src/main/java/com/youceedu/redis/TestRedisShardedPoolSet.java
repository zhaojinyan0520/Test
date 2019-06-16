package com.youceedu.redis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class TestRedisShardedPoolSet {
	//创建实体类
	private static ShardedJedisPool jedisPool = null;
	
	static{
		try {
			if (jedisPool==null) {
				//设置第一个参数
				GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
				poolConfig.setMaxTotal(10);
				poolConfig.setMaxIdle(10);
				poolConfig.setMinIdle(10);
				poolConfig.setMaxWaitMillis(2000);
				poolConfig.setTestOnBorrow(false);
				poolConfig.setTestOnReturn(false);
				
				//设置参数2
				List<JedisShardInfo> shardedinfo = new ArrayList<JedisShardInfo>();
				shardedinfo.add(new JedisShardInfo("192.168.228.128",6379));
				
				new ShardedJedisPool(poolConfig, shardedinfo);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	//借连接
	public static synchronized ShardedJedis getshardedJedis(){
		ShardedJedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return jedis;
	}
	
	//归还连接
	public static void returnShardedJedis(ShardedJedis shardedJedis){
		try {
			jedisPool.returnResourceObject(shardedJedis);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static String setRedis(String key,String value){
		ShardedJedis shardedJedis = null;
		String result = null;
		try {
			shardedJedis = getshardedJedis();
			result = shardedJedis.set(key, value);

		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			returnShardedJedis(shardedJedis);
		}
		return result;
		
	}
	public static void main(String[] args) {
		String tmp = setRedis("score", "30");
		System.out.println(tmp);
	}

}
