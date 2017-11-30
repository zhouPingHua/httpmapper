package com.httpmapper;

import com.httpmapper.handler.PostProcessor;
import com.httpmapper.http.MapperRequest;

/**
 * @author zph  on 2017/11/29
 */
public class TestPostProcessor implements PostProcessor {
    @Override
    public void handlerBefore(MapperRequest request, Object objectParam) {
        System.out.println("TestPostProcessor  前置执行");
    }

    @Override
    public Object handleAfter(MapperRequest mapperRequest, Object objectParam, Object object) {
        System.out.println("TestPostProcessor  后置执行");
        return object;
    }
}
