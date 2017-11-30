package com.httpmapper.annotation;

import com.httpmapper.http.FastJsonResponseHandler;
import com.httpmapper.http.ResponseHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zph  on 2017/11/29
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Response {
    Class<? extends ResponseHandler> value() default FastJsonResponseHandler.class;
}
