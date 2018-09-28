package com.wlb.forever.toptraffic.repository;

import com.wlb.forever.toptraffic.domain.VisitMonitor;
import com.wlb.forever.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;


public class RedisTopTrafficRepository extends AbstractTopTrafficRepository {
    private RedisUtil redisUtil;

    public RedisTopTrafficRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisUtil = new RedisUtil(redisTemplate);
    }

    @Override
    public void insertVisitMonitor(VisitMonitor visitMonitor) {
        redisUtil.lSet("visitlog", visitMonitor);
    }
}
