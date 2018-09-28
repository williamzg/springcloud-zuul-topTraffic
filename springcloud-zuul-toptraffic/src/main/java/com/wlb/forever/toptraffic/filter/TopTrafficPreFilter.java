package com.wlb.forever.toptraffic.filter;

import com.netflix.zuul.context.RequestContext;
import com.wlb.forever.toptraffic.support.TopTrafficConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;

public class TopTrafficPreFilter extends AbstractTopTrafficFilter {
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        final HttpServletResponse response = ctx.getResponse();
        final HttpServletRequest request = ctx.getRequest();
        request.setAttribute(TopTrafficConstants.REQUEST_START_TIME, System.currentTimeMillis());
        return null;
    }
}
