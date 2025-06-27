package com.cloudnrg.api.shared.infrastructure.cache.redis.configuration;

import com.giffing.bucket4j.spring.boot.starter.config.cache.SyncCacheResolver;
import com.giffing.bucket4j.spring.boot.starter.config.cache.jcache.JCacheCacheResolver;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import org.redisson.config.Config;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.cache.CacheManager;
import javax.cache.Caching;

@Configuration
public class RedisConfig {
    @Bean
    public Config config(@Value("${spring.data.redis.host:localhost}") String host,
                         @Value("${spring.data.redis.port:6379}") String port){
        Config config=new Config();
        config.useSingleServer().setAddress(String.format("redis://%s:%s", host, port));
        return config;
    }

    @Bean
    public CacheManager cacheManager(Config config){
        CacheManager manager = Caching.getCachingProvider().getCacheManager();
        manager.createCache("cache", RedissonConfiguration.fromConfig(config));
        return manager;
    }
    @Bean
    ProxyManager<String> proxyManager(CacheManager cacheManager){
        return new JCacheProxyManager<>(cacheManager.getCache("cache"));
    }

    @Bean
    @Primary
    public SyncCacheResolver bucket4jCacheResolver(CacheManager cacheManager){
        return new JCacheCacheResolver(cacheManager);
    }
}
