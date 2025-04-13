package com.likelion13.lucaus_api.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(redisPort);
        if (!redisPassword.isEmpty()) {
            jedisConnectionFactory.setPassword(redisPassword);
        }
        return jedisConnectionFactory;
    }

    @Bean
    public CacheManager cacheManager() {
        RedisCacheConfiguration oneMinuteCache = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(1))  // 1분 TTL
                .disableCachingNullValues();

        RedisCacheConfiguration fiveMinutesCache = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))  // 5분 TTL
                .disableCachingNullValues();

        RedisCacheConfiguration tenMinutesCache = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))  // 10분 TTL
                .disableCachingNullValues();
        RedisCacheConfiguration oneHourCache = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))  // 1시간 TTL
                .disableCachingNullValues();

        return RedisCacheManager.builder(jedisConnectionFactory())
                .withCacheConfiguration("short_1min", oneMinuteCache)
                .withCacheConfiguration("detail_5min", fiveMinutesCache)
                .withCacheConfiguration("lost_10min", tenMinutesCache)
                .withCacheConfiguration("detail_1hour", oneHourCache)
                .build();
    }
}
