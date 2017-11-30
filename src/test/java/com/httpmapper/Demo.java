package com.httpmapper;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zph  Date：2017/11/7.
 */
@Data
public class Demo implements Serializable {

    private Long id;
    private String name;
    private Integer age;
    private String address;

    public Demo() {
    }

    public Demo(Long id, String name, Integer age, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    @Override
    public String toString(){
        return id+" "+name+" "+age+" "+address;
    }
}
