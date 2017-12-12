package com.httpmapper.proxy;

import com.httpmapper.config.Configuration;

import java.lang.reflect.Proxy;

/**
 * @author zph  on 2017/11/29
 */
public class MapperProxyFactory {

    public static <T> T newInstance(Class<T> clazz, Configuration configuration) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] {clazz}, new MapperProxy(configuration));
    }
}
