package com.wlb.forever.toptraffic.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
@Data
public class VisitMonitor implements Serializable {
    private static final long serialVersionUID = 5423159183988980765L;

    /**
     * 请求URL
     */
    private String requestUrl;
    /**
     * 请求IP
     */
    private String requestIp;
    /**
     * 请求时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date visitTime;
    /**
     * 请求耗时
     */
    private Long timeConsum;
    /**
     * 请求流量
     */
    private Integer requestTraffic;
    /**
     * 返回HTTP码
     */
    private Integer responseCode;
    /**
     * 返回数据
     */
    private byte[] responseData;
}
