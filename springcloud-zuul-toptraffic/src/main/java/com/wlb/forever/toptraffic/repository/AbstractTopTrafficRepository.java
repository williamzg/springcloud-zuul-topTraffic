package com.wlb.forever.toptraffic.repository;

import com.wlb.forever.toptraffic.domain.VisitMonitor;

public abstract class AbstractTopTrafficRepository implements TopTrafficRepository {
    @Override
    public void saveVisitMonitor(VisitMonitor visitMonitor) {
        insertVisitMonitor(visitMonitor);
    }

    protected abstract void insertVisitMonitor(VisitMonitor visitMonitor);
}
