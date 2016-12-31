package com.flashrpc.core;

/**
 * Created by yeyc on 2016/12/31.
 */
public interface AbstractChannel {
    void shutdown();

    void sendMsg(byte[]  msg);
}
