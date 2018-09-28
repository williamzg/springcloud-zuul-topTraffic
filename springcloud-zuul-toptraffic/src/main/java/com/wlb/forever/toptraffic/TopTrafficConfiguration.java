package com.wlb.forever.toptraffic;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.wlb.forever.toptraffic.repository.RedisTopTrafficRepository;
import com.wlb.forever.toptraffic.repository.TopTrafficRepository;
import com.wlb.forever.toptraffic.filter.TopTrafficPostFilter;
import com.wlb.forever.toptraffic.filter.TopTrafficPreFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static com.wlb.forever.toptraffic.support.TopTrafficConstants.PREFIX;

@Configuration
public class TopTrafficConfiguration {
    @Bean
    public ZuulFilter topTrafficPreFilter() {
        return new TopTrafficPreFilter();
    }

    @Bean
    public ZuulFilter topTrafficPostFilter(TopTrafficRepository topTrafficRepository) {
        return new TopTrafficPostFilter(topTrafficRepository);
    }

    @Configuration
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnMissingBean(TopTrafficRepository.class)
    @ConditionalOnProperty(prefix = PREFIX, name = "repository", havingValue = "REDIS")
    public static class RedisConfiguration {

        @Bean("topTrafficRedisTemplate")
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
            RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
            template.setConnectionFactory(factory);
            Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            jackson2JsonRedisSerializer.setObjectMapper(om);
            StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
            // key采用String的序列化方式
            template.setKeySerializer(stringRedisSerializer);
            // hash的key也采用String的序列化方式
            template.setHashKeySerializer(stringRedisSerializer);
            // value序列化方式采用jackson
            template.setValueSerializer(jackson2JsonRedisSerializer);
            // hash的value序列化方式采用jackson
            template.setHashValueSerializer(jackson2JsonRedisSerializer);
            template.afterPropertiesSet();
            return template;
        }

        @Bean
        public TopTrafficRepository redisTopTraffic(@Qualifier("topTrafficRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
            return new RedisTopTrafficRepository(redisTemplate);
        }
    }


}
