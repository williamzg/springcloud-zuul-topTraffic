package com.wlb.forever.toptraffic.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopTrafficConstants {
    public static final String HEADER_QUOTA = "X-TopTraffic-Quota-";
    public static final String HEADER_REMAINING_QUOTA = "X-TopTraffic-Remaining-Quota-";
    public static final String HEADER_LIMIT = "X-TopTraffic-Limit-";
    public static final String HEADER_REMAINING = "X-TopTraffic-Remaining-";
    public static final String HEADER_RESET = "X-TopTraffic-Reset-";
    public static final String REQUEST_START_TIME = "TopTrafficRequestStartTime";
}
