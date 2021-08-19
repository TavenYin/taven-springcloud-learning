package com.github.taven.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class ReactiveRedisConfiguration {

    private RedisSerializationContext<String, String> redisSerializationContext() {
        RedisSerializationContext.RedisSerializationContextBuilder<String, String> builder = RedisSerializationContext.newSerializationContext();
        builder.key(StringRedisSerializer.UTF_8);
//        builder.value(RedisSerializer.json());
        builder.value(RedisSerializer.string());
        builder.hashKey(StringRedisSerializer.UTF_8);
        builder.hashValue(StringRedisSerializer.UTF_8);
        return builder.build();
    }

    @Bean
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializationContext<String, String> serializationContext = redisSerializationContext();
        return new ReactiveRedisTemplate<>(connectionFactory,serializationContext);
    }

}
