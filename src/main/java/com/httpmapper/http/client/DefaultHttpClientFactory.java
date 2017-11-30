package com.httpmapper.http.client;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author zph  on 2017/11/29
 */
public class DefaultHttpClientFactory implements HttpClientFactory {
    @Override
    public CloseableHttpClient creat() {
        return HttpClients.createDefault();
    }
}
