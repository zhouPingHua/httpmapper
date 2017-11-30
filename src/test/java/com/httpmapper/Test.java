package com.httpmapper;

import com.alibaba.fastjson.JSONObject;
import com.httpmapper.config.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zph  on 2017/11/29
 */
public class Test {

    @org.junit.Test
    public void test(){
        Configuration configuration  = Configuration.newBuilder().setScanPath("").build();
        TestMapper testMapper = configuration.newMapper(TestMapper.class);
        Demo testBean = new Demo();
        testBean.setId(18l);
        testBean.setAddress("mrchenli");
        testBean.setName("zph");
        Demo obj = testMapper.home(testBean);
        System.out.println(JSONObject.toJSONString(obj));
    }

    @org.junit.Test
    public void test2(){
        Configuration configuration  = Configuration.newBuilder().setScanPath("com.httpmapper").build();
        TestMapper testMapper = configuration.newMapper(TestMapper.class);
        Demo obj = testMapper.home2("11",12);
        System.out.println(JSONObject.toJSONString(obj));
    }
}
