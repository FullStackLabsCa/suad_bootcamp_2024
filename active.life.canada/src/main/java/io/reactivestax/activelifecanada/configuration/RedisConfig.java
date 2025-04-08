package io.reactivestax.activelifecanada.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.reactivestax.activelifecanada.dto.CartDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, CartDto> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, CartDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Build ObjectMapper with Java Time support
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule()) // for LocalDate, LocalTime, etc.
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Jackson2JsonRedisSerializer<CartDto> serializer = new Jackson2JsonRedisSerializer<>(mapper, CartDto.class);

        template.setDefaultSerializer(serializer);
        template.setValueSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }
}
