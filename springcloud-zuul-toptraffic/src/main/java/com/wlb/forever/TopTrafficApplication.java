package com.wlb.forever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableZuulProxy
@EnableFeignClients
//@EnableRedisHttpSession  
public class TopTrafficApplication {

    public static void main(String[] args) {
        SpringApplication.run(TopTrafficApplication.class, args);
    }

}
