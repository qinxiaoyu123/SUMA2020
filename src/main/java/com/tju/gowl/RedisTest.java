package com.tju.gowl;
//import redis.clients.jedis.Jedis;
//
//public class RedisTest {
//    public static void main(String[] args) {
//        //连接本地的 Redis 服务
//        Jedis jedis = new Jedis("localhost");
//        System.out.println("连接成功");
//        //查看服务是否运行
//        System.out.println("服务正在运行: "+jedis.ping());
//
//        jedis.set("key001", String.valueOf(1));
//        if(jedis.exists("key001")){
//            int i = Integer.parseInt(jedis.get("key001"));
//            System.out.println(i);
//        }
//
//    }
//
//}
