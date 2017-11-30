package com.httpmapper.annotation;

import com.httpmapper.handler.PostProcessor;
import com.httpmapper.http.MapperRequest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zph  on 2017/11/29
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PostProcess {

    Class<? extends PostProcessor> [] value() default DoNothingPostProcessor.class;

    final class DoNothingPostProcessor implements PostProcessor {

        @Override
        public void handlerBefore(MapperRequest request, Object objectParam) {
            return;
        }

        @Override
        public Object handleAfter(MapperRequest request, Object objectParam, Object object) {
            return object;
        }
    }
}
