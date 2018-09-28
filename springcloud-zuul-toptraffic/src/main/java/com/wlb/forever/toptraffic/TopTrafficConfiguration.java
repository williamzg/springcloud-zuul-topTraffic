package com.wlb.forever.toptraffic;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.wlb.forever.toptraffic.service.impl.JpaTopTrafficServiceImpl;
import com.wlb.forever.toptraffic.service.impl.RedisTopTrafficServiceImpl;
import com.wlb.forever.toptraffic.service.TopTrafficService;
import com.wlb.forever.toptraffic.filter.TopTrafficPostFilter;
import com.wlb.forever.toptraffic.filter.TopTrafficPreFilter;
import com.wlb.forever.toptraffic.support.TopTrafficCrudRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
    public ZuulFilter topTrafficPostFilter(TopTrafficService topTrafficService) {
        return new TopTrafficPostFilter(topTrafficService);
    }

    @Configuration
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnMissingBean(TopTrafficService.class)
    @ConditionalOnProperty(prefix = PREFIX, name = "service", havingValue = "REDIS")
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
        public TopTrafficService redisTopTrafficServiceImpl(@Qualifier("topTrafficRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
            return new RedisTopTrafficServiceImpl(redisTemplate);
        }
    }

    @EntityScan
    @Configuration
    @EnableJpaRepositories
    @ConditionalOnMissingBean(TopTrafficService.class)
    @ConditionalOnProperty(prefix = PREFIX, name = "service", havingValue = "JPA")
    public static class SpringDataConfiguration {
        @Bean
        public TopTrafficService jpaTopTrafficServiceImpl(TopTrafficCrudRepository topTrafficCrudRepository) {
            return new JpaTopTrafficServiceImpl(topTrafficCrudRepository);
        }
    }
}
