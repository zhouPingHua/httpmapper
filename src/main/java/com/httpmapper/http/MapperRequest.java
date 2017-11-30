package com.httpmapper.http;

import com.google.common.collect.Lists;
import com.httpmapper.handler.PostProcessor;
import com.httpmapper.http.type.EntityType;
import com.httpmapper.http.type.HttpMethod;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author zph  on 2017/11/29
 */
@Data
public class MapperRequest {

    private RequestInfo requestInfo;
    private HttpMethod httpMethod;
    private EntityType entityType;
    private Type returnType;
    private ResponseHandler responseHandler;//解析返回值得 序列化 json String什么的
    private List<PostProcessor> postProcessors = Lists.newArrayList() ;//返回值得拦截器列表 //这个是可选的


}
