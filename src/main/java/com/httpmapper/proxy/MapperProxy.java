package com.httpmapper.proxy;

import com.google.common.collect.Maps;
import com.google.common.reflect.AbstractInvocationHandler;
import com.httpmapper.annotation.HttpParam;
import com.httpmapper.config.Configuration;
import com.httpmapper.handler.PostProcessor;
import com.httpmapper.http.MapperRequest;
import com.httpmapper.http.executor.HttpExecutor;
import com.httpmapper.util.MapperRequestKeyUtil;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author zph  on 2017/11/29
 *         <p>
 *         JDK代理
 */
public class MapperProxy extends AbstractInvocationHandler {

    private static Logger logger = LoggerFactory.getLogger(MapperProxy.class);

    private final HttpExecutor httpExecutor;

    private final Configuration configuration;

    public MapperProxy(Configuration configuration) {
        this.configuration = configuration;
        this.httpExecutor = configuration.getHttpExecutor();
    }

    @Override
    protected Object handleInvocation(Object o, Method method, Object[] args) {
        String mrKey = MapperRequestKeyUtil.getKey(method);
        MapperRequest mapperRequest = configuration.getMapperRequest(mrKey);
        checkNotNull(mapperRequest);
        Object params = resolveRequestParameter(method, args);
        //前置操作的处理
        invokePostProcessBefore(mapperRequest, params, mapperRequest.getPostProcessors());
        HttpResponse response = httpExecutor.executor(mapperRequest, params);
        logger.info("response statusline is ==>{}", response.getStatusLine());
        Object target = mapperRequest.getResponseHandler().handle(mapperRequest, response);
        //执行后置的操作 然后返回
        return invokePostProcessAfter(mapperRequest, params, target, mapperRequest.getPostProcessors());

    }


    private Object resolveRequestParameter(Method method, Object[] args) {
        try {
            final Object paramObject;
            if (args == null || args.length == 0) {
                paramObject = Collections.emptyMap();
            } else {
                final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                final Map<String, Object> tmpParams = Maps.newHashMapWithExpectedSize(args.length);
                boolean isParamAnnotation = false;
                outer:
                for (int i = 0; i < parameterAnnotations.length; i++) {
                    Annotation[] annotations = parameterAnnotations[i];
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof HttpParam) {
                            tmpParams.put(((HttpParam) annotation).value(), args[i]);
                            isParamAnnotation = true;
                            continue outer;
                        }
                    }
                }
                paramObject = isParamAnnotation ? tmpParams : args.length == 1 ? args[0] : Collections.emptyMap();
            }
            return paramObject;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void invokePostProcessBefore(MapperRequest mapperRequest, Object objectParam, List<PostProcessor> processors) {
        for (PostProcessor postProcessor : processors) {
            postProcessor.handlerBefore(mapperRequest, objectParam);
        }
    }

    public Object invokePostProcessAfter(MapperRequest request, Object objectParam, Object target, List<PostProcessor> processors) {
        for (PostProcessor postProcessor : processors) {
            target = postProcessor.handleAfter(request, objectParam, target);
        }
        return target;
    }
}
