package com.wlb.forever.toptraffic;

import com.netflix.zuul.ZuulFilter;
import com.wlb.forever.toptraffic.filter.TopTrafficPostFilter;
import com.wlb.forever.toptraffic.filter.TopTrafficPreFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopTrafficConfiguration {
    @Bean
    public ZuulFilter topTrafficPreFilter() {
        return new TopTrafficPreFilter();
    }

    @Bean
    public ZuulFilter topTrafficPostFilter() {
        return new TopTrafficPostFilter();
    }
}
