package com.wlb.forever.toptraffic.service.impl;

import com.wlb.forever.toptraffic.domain.VisitMonitor;
import com.wlb.forever.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisTopTrafficServiceImpl extends AbstractTopTrafficServiceImpl {
    private RedisUtil redisUtil;

    public RedisTopTrafficServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisUtil = new RedisUtil(redisTemplate);
    }

    @Override
    public void insertVisitMonitor(VisitMonitor visitMonitor) {
        redisUtil.lSet("visitlog", visitMonitor);
    }
}
