package com.httpmapper.http;

import lombok.Data;

/**
 * @author zph  on 2017/11/29
 *
 */
@Data
public class RequestInfo {

    //请求url
    private String url;
    //url编码
    private String urlEncoding;
    //超时时间（秒）
    private long timeout;

}
