package com.httpmapper.handler;

import com.httpmapper.http.MapperRequest;

/**
 * @author zph  on 2017/11/29
 */
public interface PostProcessor {

    void handlerBefore(MapperRequest request, Object objectParam);

    Object handleAfter(MapperRequest mapperRequest,Object objectParam,Object object);

}
