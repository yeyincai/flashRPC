package com.flashrpc.serialize.protostuff;

/**
 * Created by yeyc on 2016/12/29.
 */
public class Pojo {
    private int age;
    private String name;
    public Pojo(){}

    public Pojo(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
