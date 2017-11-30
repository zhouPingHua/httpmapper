package com.httpmapper;

import com.httpmapper.annotation.*;
import com.httpmapper.http.type.EntityType;


@HttpMapper
public interface TestMapper {//可以产生一个代理对象 在执行的时候收集信息还是初始化的时候收集信息

    @Request("http://localhost:8080/demoAop.go")
    @POST(entity = EntityType.FORM)
    @PostProcess(TestPostProcessor.class)
    Demo home(Demo testBean);



    @Request("http://localhost:8080/demoAop2.go")
    @POST(entity = EntityType.FORM)
    Demo home2(@HttpParam("name") String name, @HttpParam("age") int age);


}
