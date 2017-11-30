package com.httpmapper.config;

import com.httpmapper.exception.ConfigurationBuilderException;
import com.httpmapper.http.MapperRequest;
import com.httpmapper.http.MapperRequestFactory;
import com.httpmapper.http.client.DefaultHttpClientFactory;
import com.httpmapper.http.executor.DefaultHttpExecutor;
import com.httpmapper.http.executor.HttpExecutor;
import com.httpmapper.proxy.ServiceProxyFactory;

/**
 * @author zph  on 2017/11/29
 */
public class Configuration {

    private HttpExecutor httpExecutor = new DefaultHttpExecutor(new DefaultHttpClientFactory());

    private MapperRequestFactory mapperRequestFactory;

    public HttpExecutor getHttpExecutor() {
        return httpExecutor;
    }

    public MapperRequest getMapperRequest(String key){
        return mapperRequestFactory.getMapperRequest(key);
    }

    private Configuration(HttpExecutor httpExecutor,MapperRequestFactory mapperRequestFactory){
        this.httpExecutor = httpExecutor;
        this.mapperRequestFactory = mapperRequestFactory;
    }

    public <T> T newMapper(Class<T> mapperClass) {
        return ServiceProxyFactory.newInstance(mapperClass, this);
    }

    public static ConfigurationBuilder newBuilder() {
        return new ConfigurationBuilder();
    }

    public static class ConfigurationBuilder{

        private HttpExecutor httpExecutor;
        private String scanPath;

        private ConfigurationBuilder() {
        }

        public  ConfigurationBuilder setHttpExecutor(HttpExecutor httpExecutor){
            this.httpExecutor = httpExecutor;
            return this;
        }
        public ConfigurationBuilder setScanPath(String scanPath){
            this.scanPath = scanPath;
            return this;
        }

        public Configuration build(){
            if(httpExecutor==null){
                httpExecutor = new DefaultHttpExecutor(new DefaultHttpClientFactory());
            }
            if(scanPath == null || scanPath.trim().length()==0){
                throw new ConfigurationBuilderException("请指定third mapper 的扫描路径");
            }
            MapperRequestFactory mapperRequestFactory = new MapperRequestFactory(scanPath);
            return new Configuration(httpExecutor,mapperRequestFactory);
        }
    }

}
