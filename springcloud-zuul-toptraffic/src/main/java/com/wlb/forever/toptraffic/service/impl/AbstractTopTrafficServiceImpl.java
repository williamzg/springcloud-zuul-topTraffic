package com.wlb.forever.toptraffic.service.impl;

import com.wlb.forever.toptraffic.domain.VisitMonitor;
import com.wlb.forever.toptraffic.service.TopTrafficService;

public abstract class AbstractTopTrafficServiceImpl implements TopTrafficService {
    @Override
    public void saveVisitMonitor(VisitMonitor visitMonitor) {
        insertVisitMonitor(visitMonitor);
    }

    protected abstract void insertVisitMonitor(VisitMonitor visitMonitor);
}
