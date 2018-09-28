package com.wlb.forever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class TopTrafficApplication {

    public static void main(String[] args) {
        SpringApplication.run(TopTrafficApplication.class, args);
    }

}
