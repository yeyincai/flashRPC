package com.flashrpc.core;

/**
 * Created by yeyc on 2016/12/28.
 */
public interface Serializer {

    <T> byte[] serializer(T obj);

    <T> T deserializer(byte[] data, Class<T> clazz);
}
