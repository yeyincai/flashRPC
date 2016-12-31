package com.flashrpc.core.client;

import com.flashrpc.core.metadata.RpcRequest;

/**
 * Created by yeyc on 2016/12/31.
 */
public interface ClientMessageHandler {

    void receiveAndProcessor(byte[] request);

    Object sendAndProcessor(RpcRequest rpcRequest) throws InterruptedException;
}
