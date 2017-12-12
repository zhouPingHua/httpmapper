package com.httpmapper.http.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.httpmapper.http.MapperRequest;
import com.httpmapper.http.client.HttpClientFactory;
import com.httpmapper.http.type.EntityType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author zph  on 2017/11/29
 */
public class DefaultHttpExecutor implements HttpExecutor,AutoCloseable{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHttpExecutor.class);
    private HttpClient httpClient;

    public DefaultHttpExecutor(HttpClientFactory factory) {
        this.httpClient = factory.creat();
    }

    @Override
    public HttpResponse executor(MapperRequest mappedRequest, Object params) {
        try {
            checkNotNull(mappedRequest);
            HttpUriRequest httpUriRequest = buildHttpRequest(mappedRequest,params);
            LOGGER.debug("execute http request:mapperRequest={}, httpUriRequest={}", JSONObject.toJSONString(mappedRequest), JSONObject.toJSONString(httpUriRequest));
            HttpResponse response = httpClient.execute(httpUriRequest);
            return response;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        System.out.println("============================closeable test");
        if(this.httpClient instanceof Closeable){
            ((Closeable) this.httpClient).close();
        }
    }

    /**
     * 根据注解配置的请求
     * @param request
     * @param params
     * @return
     */
    private HttpUriRequest buildHttpRequest(MapperRequest request,Object params){
        try {
            switch (request.getHttpMethod()){

                case GET:{
                    final HttpGet httpGet = new HttpGet();
                    httpGet.setURI(new URI(request.getRequestInfo().getUrl()));
                    return httpGet;
                }

                case POST:{
                    final HttpPost httpPost = new HttpPost();
                    httpPost.setEntity(createHttpEntity(params,request.getEntityType()));
                    httpPost.setURI(new URI(request.getRequestInfo().getUrl()));
                    return httpPost;
                }
                default:
                    throw new UnsupportedOperationException("不支持的http method :"+request.getHttpMethod());
            }
        }catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpEntity createHttpEntity(Object params , EntityType entityType) throws IllegalAccessException {
        switch (entityType){
            case JSON_STRING:
                try {
                    return new StringEntity(JSON.toJSONString(params));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            case FORM:
            default:
                Map<String,Object> par;
                if(params instanceof Map){
                    par = (Map<String, Object>) params;
                }else{
                    Map<String,Object> tempParams = Maps.newHashMap();
                    for(Class<?> clazz = params.getClass() ; clazz != Object.class ; clazz = clazz.getSuperclass()) {
                        try {
                            Field[] fields = clazz.getDeclaredFields();
                            for (Field f :fields) {
                                f.setAccessible(true);
                                if(f.get(params)==null){
                                    continue;
                                }
                                tempParams.put(f.getName(),f.get(params));
                                f.setAccessible(false);
                            }
                        } catch (Exception e) {
                        }
                    }
                    par = tempParams;
                }
                return new UrlEncodedFormEntity(
                        Iterables.transform(par.entrySet(),
                                (Function<Map.Entry<String, Object>, NameValuePair>) en -> new BasicNameValuePair(en.getKey(), String.valueOf(en.getValue())))
                );
        }

    }
}
