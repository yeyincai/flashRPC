package com.flashrpc.core;

/**
 * Created by yeyc on 2016/12/30.
 */
public interface MessageHandler {
    void readAndProcessor(byte[] request);
}
