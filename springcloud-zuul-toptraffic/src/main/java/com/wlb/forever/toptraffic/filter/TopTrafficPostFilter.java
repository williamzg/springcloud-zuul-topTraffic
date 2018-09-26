package com.wlb.forever.toptraffic.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wlb.forever.toptraffic.domain.VisitMonitor;
import com.wlb.forever.toptraffic.support.TopTrafficConstants;
import com.wlb.forever.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;

public class TopTrafficPostFilter extends ZuulFilter {
    
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_RESPONSE_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        int requestTraffic=getResponseDataStreamSize();
        String requestIp=getIpAddr(request);
        String requestUrl=getRequestAddr(request);
        Long timeConsum = System.currentTimeMillis() - getRequestStartTime();
        VisitMonitor visitMonitor=new VisitMonitor();
        visitMonitor.setRequestIp(requestIp);
        visitMonitor.setRequestTraffic(requestTraffic);
        visitMonitor.setRequestUrl(requestUrl);
        visitMonitor.setResponseCode(ctx.getResponseStatusCode());
        visitMonitor.setResponseData(null);
        visitMonitor.setTimeConsum(timeConsum);
        Date date=new Date();
        visitMonitor.setVisitTime(date);
        redisUtil.lSet("visitlog",visitMonitor);
        return null;
    }

    /**
     *获取返回数据流量
     * @return
     */
    private int getResponseDataStreamSize(){
        final RequestContext ctx = RequestContext.getCurrentContext();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int responseDataStreamSize=0;
        try {
            InputStream is = ctx.getResponseDataStream();
            baos = cloneInputStream(is);
            responseDataStreamSize=baos.size();
            final InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
            ctx.setResponseDataStream(inputStream);
            baos.close();
            return responseDataStreamSize;
        } catch (Exception e) {
            e.printStackTrace();
            return responseDataStreamSize;
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    System.out.println("ByteArrayOutputStream关闭失败!");
                    return responseDataStreamSize;
                }
            }
        }
    }

    /**
     * 获取请求开始时间
     * @return
     */
    private Long getRequestStartTime() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        final HttpServletRequest request = ctx.getRequest();
        return (Long) request.getAttribute(TopTrafficConstants.REQUEST_START_TIME);
    }

    /**
     * InputSteam转ByteArrayOutputStream
     *
     * @param input
     * @return
     */
    private ByteArrayOutputStream cloneInputStream(InputStream input) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 获取访问请求ip
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取请求URL
     * @param request
     * @return
     */
    private String getRequestAddr(HttpServletRequest request){
        String requestPath = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (queryString != null) {
            return requestPath + "?" + request.getQueryString();
        } else {
            return requestPath;
        }
    }

}
