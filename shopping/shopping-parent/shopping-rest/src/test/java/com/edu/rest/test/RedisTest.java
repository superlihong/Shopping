package com.edu.rest.test;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RedisTest {
    @Test
    public void test(){
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("192.168.152.130",6379);
        System.out.println("连接成功");
        //设置 redis 字符串数据
        jedis.set("runoobkey", "www.runoob.com");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("runoobkey"));
    }
    @Test
    public void test2(){
        //连接 Redis
        JedisPool pool=new JedisPool("192.168.152.130",6379);

        Jedis jedis=pool.getResource();
        System.out.println("连接成功");
        //设置 redis 字符串数据
        jedis.set("runoobkey1", "www.runoob.com1");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("runoobkey1"));
    }
    //集群
    @Test
    public void test3(){
        Set<HostAndPort> nodes=new HashSet<>();
        nodes.add(new HostAndPort("192.168.152.130",7001));
        nodes.add(new HostAndPort("192.168.152.130",7002));
        nodes.add(new HostAndPort("192.168.152.130",7003));
        nodes.add(new HostAndPort("192.168.152.130",7004));
        nodes.add(new HostAndPort("192.168.152.130",7005));
        nodes.add(new HostAndPort("192.168.152.130",7006));
        JedisCluster cluster=new JedisCluster(nodes);
        cluster.set("cluster","cluster1");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ cluster.get("cluster"));
        cluster.close();
    }
}
