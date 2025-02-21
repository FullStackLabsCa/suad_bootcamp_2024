package io.reactivestax.active_life_canada.configuration;

import io.reactivestax.active_life_canada.dto.CartDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, CartDto> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, CartDto> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(CartDto.class));
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }
}
