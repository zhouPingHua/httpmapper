package com.httpmapper.http;

import com.httpmapper.annotation.*;
import com.httpmapper.handler.PostProcessor;
import com.httpmapper.http.type.HttpMethod;
import com.httpmapper.util.MapperRequestKeyUtil;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author zph  on 2017/11/29
 */
public class MapperRequestFactory {

    private Map<String,MapperRequest> mapperRequests = new ConcurrentHashMap<>();

    public MapperRequestFactory(String classPath) {
        init(classPath);
    }

    public void init(String classPath){
        try {
            Reflections reflections = new Reflections(classPath);
            Set<Class<?>> HttpMappers = reflections.getTypesAnnotatedWith(HttpMapper.class);
//            if(HttpMappers.size() == 0)
            for (Class<?> HttpMapper:HttpMappers) {
                Method[] methods = HttpMapper.getDeclaredMethods();
                for (Method method:methods) {
                    if(!method.isAnnotationPresent(Request.class)){
                        continue;
                    }
                    MapperRequest mapperRequest = parseMethodToMapperRequest(method);
                    checkNotNull(mapperRequest);
                    String key = MapperRequestKeyUtil.getKey(method);
                    checkNotNull(key);
                    mapperRequests.put(key,mapperRequest);
                }
            }
        }catch (Exception e){
            throw new RuntimeException("mapperFactory 初始化异常",e);
        }
    }



    private MapperRequest parseMethodToMapperRequest(Method method) throws IllegalAccessException, InstantiationException {
        MapperRequest mapperRequest = new MapperRequest();
        //1.收集request 的url
        RequestInfo requestInfo = new RequestInfo();
        //解析@Request
        Request request = method.getAnnotation(Request.class);
        requestInfo.setUrl(request.value());
        requestInfo.setUrlEncoding(request.urlEncoding());
        requestInfo.setTimeout(request.timeout());
        mapperRequest.setRequestInfo(requestInfo);

        //解析其他
        if(method.isAnnotationPresent(GET.class)){
            mapperRequest.setHttpMethod(HttpMethod.GET);
        }

        if(method.isAnnotationPresent(POST.class)){
            mapperRequest.setHttpMethod(HttpMethod.POST);
            POST post = method.getAnnotation(POST.class);
            mapperRequest.setEntityType(post.entity());
        }

        //解析序列化
        if(method.isAnnotationPresent(Response.class)){
            Response response = method.getAnnotation(Response.class);
            mapperRequest.setResponseHandler(response.value().newInstance());
        }else{
            mapperRequest.setResponseHandler(new FastJsonResponseHandler());
        }

        //解析拦截器了
        if(method.isAnnotationPresent(PostProcess.class)){
            Class<? extends PostProcessor>[] clzzs = method.getAnnotation(PostProcess.class).value();
            for (Class<? extends PostProcessor> clzz: clzzs) {
                mapperRequest.getPostProcessors().add(clzz.newInstance());
            }
        }
        //返回值类型
        mapperRequest.setReturnType(method.getGenericReturnType());
        return mapperRequest;
    }


    public MapperRequest getMapperRequest(String key){
        return mapperRequests.get(key);
    }
}
