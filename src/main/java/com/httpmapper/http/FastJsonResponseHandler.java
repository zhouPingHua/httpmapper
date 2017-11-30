package com.httpmapper.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author zph  on 2017/11/29
 */
public class FastJsonResponseHandler implements ResponseHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FastJsonResponseHandler.class);

    @Override
    public Object handle(MapperRequest request, HttpResponse response) {
        final String text;
        final HttpEntity entity = response.getEntity();
        try {
            text =  EntityUtils.toString(entity);
            LOGGER.debug("request result: request={}, result={}", request, text);
            final Type returnType = request.getReturnType();
            if (returnType instanceof Class) {
                return JSON.parseObject(text, (Class<? extends Object>) returnType);
            }
            if(returnType instanceof ParameterizedType){
                return JSON.parseObject(text,returnType);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
