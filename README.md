# 基于springcloudzuul的访问监控

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
