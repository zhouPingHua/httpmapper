package com.httpmapper.http.executor;

import com.httpmapper.http.MapperRequest;
import org.apache.http.HttpResponse;

/**
 * @author zph  on 2017/11/29
 */
public interface HttpExecutor {

    HttpResponse executor(MapperRequest mappedRequest, Object params);
}
