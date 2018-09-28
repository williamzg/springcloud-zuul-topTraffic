package com.wlb.forever.toptraffic.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 *
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ZUUL_VISIT_MONITOR")
public class VisitMonitor implements Serializable {
    private static final long serialVersionUID = 5423159183988980765L;

    @Id
    @Column(name="ID")
    @Valid
    @NotNull
    private String id;

    /**
     * 请求URL
     */
    @Column(name="REQUEST_URL")
    private String requestUrl;
    /**
     * 请求IP
     */
    @Column(name="REQUEST_IP")
    private String requestIp;
    /**
     * 请求时间
     */
    @Column(name="VISIT_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date visitTime;
    /**
     * 请求耗时
     */
    @Column(name="TIME_CONSUM")
    private Long timeConsum;
    /**
     * 请求流量
     */
    @Column(name="REQUEST_TRAFFIC")
    private Integer requestTraffic;
    /**
     * 返回HTTP码
     */
    @Column(name="RESPONSE_CODE")
    private Integer responseCode;
    /**
     * 返回数据
     */
    @Column(name="RESPONSE_DATA")
    private byte[] responseData;
}
