package com.flashrpc.core.server;



/**
 * Created by yeyc on 2016/12/30.
 */
public interface ServerMessageHandler {
    void receiveAndProcessor(byte[] request);
}
