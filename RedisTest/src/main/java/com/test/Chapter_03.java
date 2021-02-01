package com.test;

import io.lettuce.core.KeyValue;
import io.lettuce.core.RedisClient;
import io.lettuce.core.ScoredValue;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
// import redis.clients.jedis.Jedis;

public class Chapter_03 {
    // Jedis jedis = new Jedis("192.168.234.128");
    RedisClient redisClient = RedisClient.create("redis://192.168.31.124:6379/0");

    public static void main(String[] args) {
        Chapter_03 test = new Chapter_03();
        // test.test_subscribe_publish();
        // test.test_list();
    }

    @Test
    public void test_incr() {
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> commands = connect.sync();

        commands.del("ss");

        commands.set("ss", "1");
        System.out.println("ss: " + commands.get("ss"));

        commands.incr("ss");
        System.out.println("ss: " + commands.get("ss"));

        commands.incrby("ss", 100);
        System.out.println("ss: " + commands.get("ss"));

        commands.incrbyfloat("ss", 0.001F);
        System.out.println("ss: " + commands.get("ss"));

        connect.close();
        redisClient.shutdown();
    }

    @Test
    public void test_string() {
        try (StatefulRedisConnection<String, String> connect = redisClient.connect();) {
            RedisCommands<String, String> commands = connect.sync();
            commands.del("ss");

            commands.set("ss", "abcd");
            commands.append("ss", "_append");
            System.out.println("ss: " + commands.get("ss"));

            String getrange = commands.getrange("ss", 0, 3);
            System.out.println("ss getrange: " + getrange);

            commands.setrange("ss", 0, "aa");
            System.out.println("ss setrange: " + commands.get("ss"));
        }
        redisClient.shutdown();
    }

    @Test
    public void test_list() {
        try (StatefulRedisConnection<String, String> connect = redisClient.connect();) {
            RedisCommands<String, String> commands = connect.sync();
            commands.del("ss");

            commands.rpush("ss", "1");
            commands.rpush("ss", "2");
            commands.rpush("ss", "3");
            commands.rpush("ss", "4");
            commands.rpush("ss", "5");
            // commands.rpush("ss", "5");
            List<String> getList = commands.lrange("ss", 0, -1);
            System.out.println("ss: " + getList);

            String lindex = commands.lindex("ss", 4);
            System.out.println("lindex 4: " + lindex);

            String rpop = commands.rpop("ss");
            System.out.println("rpop: " + rpop + ", ss rpop: " + commands.lrange("ss", 0, -1));

            commands.lpush("ss", "0");
            System.out.println("ss lpush: " + commands.lrange("ss", 0, -1));

            commands.ltrim("ss", 0, 3);
            System.out.println("ss ltrim: " + commands.lrange("ss", 0, -1));

            System.out.println("\n测试list阻塞操作");
            commands.del("ss");
            new Thread(() -> {
                System.out.println("3");
                sleep(5);
                System.out.println("4");
                //TODO 不能用相同的connect, 否则会导致push阻塞
                // commands.rpush("ss", "test_block");
                redisClient.connect().sync().lpush("ss", "test_block");
                System.out.println("5");
            }).start();
            System.out.println("1");
            KeyValue<String, String> blpop = commands.blpop(100, "ss");
            System.out.println("2");
            System.out.println(String.format("blpop key: %s, value: %s", blpop.getKey(), blpop.getValue()));

            // 防止代码提前结束
            sleep(10);
        }
        redisClient.shutdown();
    }

    @Test
    public void test_set() {
        try (StatefulRedisConnection<String, String> connect = redisClient.connect()) {
            RedisCommands<String, String> commands = connect.sync();

            String set = "set_test";
            commands.del(set);
            commands.sadd(set, "0");
            commands.sadd(set, "0");
            commands.sadd(set, "1");
            commands.sadd(set, "2");
            commands.sadd(set, "3");
            commands.sadd(set, "4");

            Set<String> smembers = commands.smembers(set);
            System.out.println("lrange: " + smembers);

            Boolean sismember1 = commands.sismember(set, "4");
            Boolean sismember2 = commands.sismember(set, "5");
            System.out.println(String.format("sismember: 4: %s, 5: %s", sismember1, sismember2));

            commands.srem(set, "4");
            System.out.println("srem后: " + commands.smembers(set));
        }
    }

    @Test
    public void test_hash() {
        try (StatefulRedisConnection<String, String> connect = redisClient.connect()) {
            RedisCommands<String, String> commands = connect.sync();

            String key = "hash_test";
            commands.del(key);

            commands.hset(key, "hash_key_1", "value1");
            commands.hset(key, "hash_key_1", "value111111");
            commands.hset(key, "hash_key_2", "value2");
            commands.hset(key, "hash_key_3", "value3");
            commands.hset(key, "hash_key_4", "value4");

            commands.hmset(key, new HashMap<String, String>(3, 1) {{
                put("hash_key_5", "value5");
                put("hash_key_6", "value6");
                put("hash_key_7", "value7");
            }});

            String value1 = commands.hget(key, "hash_key_1");
            System.out.println("hash_key_1 value1: " + value1);

            commands.hdel(key, "hash_key_3");

            Map<String, String> hgetall = commands.hgetall(key);
            System.out.println("hash getall: " + hgetall);

            List<KeyValue<String, String>> hmget = commands.hmget(key, "hash_key_1", "hash_key_2");
            System.out.println("hmget: " + hmget);
        }
    }

    @Test
    public void test_zset() {
        try (StatefulRedisConnection<String, String> connect = redisClient.connect();) {
            RedisCommands<String, String> commands = connect.sync();

            String zset = "zset_test";
            commands.del(zset);

            commands.zadd(zset, 100, "member1");
            commands.zadd(zset, 101, "member1");
            commands.zadd(zset, 101, "member1");
            commands.zadd(zset, 102, "member1");
            commands.zadd(zset, 100, "member33333");
            commands.zadd(zset, 100, "member2");
            commands.zadd(zset, 103, "member4");
            commands.zadd(zset, 107, "member5");
            commands.zadd(zset, 104, "member6");
            commands.zadd(zset, 105, "member7");
            commands.zadd(zset, 108, "member8");
            commands.zadd(zset, 106, "member9");

            Double member4 = commands.zscore(zset, "member4");
            System.out.println("member4 score: " + member4);

            List<ScoredValue<String>> zrangeWithScores = commands.zrangeWithScores(zset, 0, -1);
            System.out.println("zrangeWithScores: " + zrangeWithScores);

            // 取出通过 zrangeWithScores 排序 (即通过score排序) 后的member
            List<String> zrange = commands.zrange(zset, 1, 3);
            System.out.println("zrange: " + zrange);

            Long member4_rank = commands.zrank(zset, "member4");
            Long member33333_rank = commands.zrank(zset, "member33333");
            System.out.println(String.format("member33333 rank: %s, member4 rank: %s", member33333_rank, member4_rank));
        }
    }

    @Test
    public void test_subscribe_publish() {
        try (
                // 需要两个connection, 一个是PubSubConnection用于pubsub, 一个普通connection用于publish
                StatefulRedisPubSubConnection<String, String> connect_pubsub = redisClient.connectPubSub();
                StatefulRedisConnection<String, String> connect_common_for_publish = redisClient.connect();
        ) {
            // 监听器
            connect_pubsub.addListener(new RedisPubSubAdapter<String, String>() {
                @Override
                public void message(String channel, String message) {
                    System.out.println("chanenl: " + channel + ", message: " + message);
                }

                @Override
                public void unsubscribed(String channel, long count) {
                    System.out.println("channel: " + channel + " unsubscribed!");
                    super.unsubscribed(channel, count);
                }
            });

            RedisPubSubCommands<String, String> commands_pubsub = connect_pubsub.sync();
            commands_pubsub.del("channel");
            RedisAsyncCommands<String, String> commands_common_for_publish = connect_common_for_publish.async();

            new Thread(() -> {
                sleep(1);
                // 被订阅的channel 发布消息
                commands_common_for_publish.publish("channel", "redis public message!");

                sleep(1);
                commands_pubsub.unsubscribe("channel");
            }).start();

            // 设置监听(订阅)的channel
            commands_pubsub.subscribe("channel");

            // 防止connect因为try自动关闭了
            sleep(5);
        }
        redisClient.shutdown();
    }

    @Test
    public void test_transaction() {
        try (StatefulRedisConnection<String, String> connect = redisClient.connect();
             StatefulRedisConnection<String, String> connect_2 = redisClient.connect();) {
            RedisCommands<String, String> commands = connect.sync();
            RedisCommands<String, String> commands_2 = connect_2.sync();

            String key = "test_tx";
            commands_2.set(key, "100");

            System.out.println("before multi: " + commands.get(key));
            commands.multi();
            commands.set(key, "100");
            System.out.println("after multi: " + commands.get(key));

            IntStream.range(0, 3)
                    .mapToObj(i -> new Thread(() -> {
                        Long before = commands.incrby(key, 1);
                        System.out.println("before: " + commands.get(key));
                        sleep(0.1);
                        Long after = commands.incrby(key, -1);
                        System.out.println("after: " + commands.get(key));
                    }))
                    .forEach(Thread::start);

            sleep(3);
            TransactionResult exec = commands.exec();
            System.out.println("exec result: " + exec);

            sleep(2);

            System.out.println("after: " + commands.get(key));
        }
        redisClient.shutdown();
    }

    private void sleep(double sleepSeconds) {
        try {
            Thread.sleep((long) (1000 * sleepSeconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
