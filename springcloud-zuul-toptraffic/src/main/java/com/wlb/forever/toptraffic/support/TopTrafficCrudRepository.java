package com.wlb.forever.toptraffic.support;

import com.wlb.forever.toptraffic.domain.VisitMonitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopTrafficCrudRepository extends JpaRepository<VisitMonitor,String> {
}
