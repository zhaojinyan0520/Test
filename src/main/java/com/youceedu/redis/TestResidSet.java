package com.youceedu.redis;

import redis.clients.jedis.Jedis;

public class TestResidSet {
	
	private static String host = "192.168.228.128";
	private static int port = 6379;
	public static String setShardedRedis(String key,String value){
		Jedis jedis = null;
		String result =null;
		try {
			jedis = new Jedis(host,port);
			result = jedis.set(key, value);
			System.out.println(result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
		
	}
	public static void main(String[] args) {
		String result = setShardedRedis("name", "wangwu");
		System.out.println(result);
	}

}
