package com.httpmapper.http;

import org.apache.http.HttpResponse;

/**
 * @author zph  on 2017/11/29
 */
public interface ResponseHandler {

    /**
     * @param request 注解配置的信息里面有返回类型
     * @param response 返回的数据呀
     * @return
     */
    Object handle(MapperRequest request, HttpResponse response);

}
