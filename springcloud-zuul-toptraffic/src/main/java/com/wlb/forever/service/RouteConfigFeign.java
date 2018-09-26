package com.wlb.forever.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "service-routeConfig")
public interface RouteConfigFeign {
    @RequestMapping(value = "/route/getRouteList")
    public String getRouteList();
}
