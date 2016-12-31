package com.flashrpc.core.server;


import com.flashrpc.core.SendMessage;

/**
 * Created by yeyc on 2016/12/30.
 */
public interface ServerMessageHandler {
    void receiveAndProcessor(byte[] request,SendMessage receiveMessage);
}
