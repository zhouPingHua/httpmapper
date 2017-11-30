package com.httpmapper.util;

import java.lang.reflect.Method;

/**
 * @author zph  on 2017/11/29
 */
public class MapperRequestKeyUtil {

    public static String getKey(Method method){
        return method.getDeclaringClass().getCanonicalName()+"."+method.getName();
    }
}
