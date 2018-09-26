package com.wlb.forever.route;

import com.wlb.forever.vo.ZuulRouteVO;
import com.wlb.forever.service.RouteConfigFeign;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.StringUtils;

import com.geostar.gtgh.DataCenter_util.HttpHelper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * <自定义路由>
 * 
 * @author  zhangguan
 * @version  [版本号, 2018年4月3日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {
    @Autowired
    private RouteConfigFeign routeConfigFeign;
    
    public final static Logger logger = LoggerFactory.getLogger(CustomRouteLocator.class);
    
    private ZuulProperties properties;
    
    @Value("${geostar.nocaszuul}")
    private String nocaszuul;
    
    public CustomRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        this.properties = properties;
        logger.info("servletPath:{}", servletPath);
    }
    
    @Override
    public void refresh() {
        doRefresh();
    }
    
    @Override
    protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>();
        //从application.properties中加载路由信息  
        routesMap.putAll(super.locateRoutes());
        //从db中加载路由信息  
        routesMap.putAll(locateRoutesFromDB());
        //优化一下配置  
        LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            // Prepend with slash if not already present.  
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasText(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            values.put(path, entry.getValue());
        }
        return values;
    }
    
    /**
     * 
     * <获取数据库保存路由配置>
     * @return [参数说明]
     * 
     * @return Map<String,ZuulProperties.ZuulRoute> [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private Map<String, ZuulProperties.ZuulRoute> locateRoutesFromDB() {
        
        HttpHelper httpHelper;
        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();
        try {
            httpHelper = new HttpHelper("http://"+nocaszuul+"/service-routeconfig/route/getRouteList");
            String results = httpHelper.Get();
            JSONObject jo = JSONObject.fromObject(results);
            JSONArray ja = jo.getJSONArray("result");
            for (int i = 0; i < ja.size(); i++) {
                JSONObject tmpJo = ja.getJSONObject(i);
                String path = tmpJo.getString("path");
                String id = tmpJo.getString("id");
                if (StringUtils.isEmpty(path) || StringUtils.isEmpty(id)) {
                    continue;
                }
                ZuulRouteVO zuulRouteVO = new ZuulRouteVO();
                zuulRouteVO.setId(tmpJo.getString("id"));
                zuulRouteVO.setPath(path);
                zuulRouteVO.setServiceId(tmpJo.getString("serviceId"));
                zuulRouteVO.setUrl(tmpJo.getString("url"));
                if (tmpJo.getInt("enabled") == 1) {
                    zuulRouteVO.setEnabled(true);
                }
                else {
                    zuulRouteVO.setEnabled(false);
                }
                if (tmpJo.getInt("retryable") == 1) {
                    zuulRouteVO.setRetryable(true);
                }
                else {
                    zuulRouteVO.setEnabled(false);
                }
                if (tmpJo.getInt("stripPrefix") == 1) {
                    zuulRouteVO.setStripPrefix(true);
                }
                else {
                    zuulRouteVO.setEnabled(false);
                }
                ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
                try {
                    BeanUtils.copyProperties(zuulRouteVO, zuulRoute);
                }
                catch (Exception e) {
                    logger.error("=============load zuul route info from db with error==============", e);
                }
                routes.put(zuulRoute.getPath(), zuulRoute);
            }
        }
        catch (Exception e1) {
            logger.error("数据库获取路由代理配置服务错误!", e1);
            e1.printStackTrace();
        }
        return routes;
    }
    
}
