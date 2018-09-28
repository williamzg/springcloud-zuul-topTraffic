package com.wlb.forever.toptraffic.service.impl;

import com.wlb.forever.toptraffic.domain.VisitMonitor;
import com.wlb.forever.toptraffic.support.TopTrafficCrudRepository;

public class JpaTopTrafficServiceImpl extends AbstractTopTrafficServiceImpl {
    private final TopTrafficCrudRepository repository;

    public JpaTopTrafficServiceImpl(TopTrafficCrudRepository topTrafficCrudRepository) {
        this.repository = topTrafficCrudRepository;
    }

    @Override
    protected void insertVisitMonitor(VisitMonitor visitMonitor) {
        repository.save(visitMonitor);
    }
}
