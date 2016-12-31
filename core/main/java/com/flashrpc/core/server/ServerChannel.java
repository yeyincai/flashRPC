package com.flashrpc.core.server;

import com.flashrpc.core.AbstractChannel;
import com.flashrpc.core.Protocol;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Created by yeyc on 2016/12/28.
 */
public interface ServerChannel extends AbstractChannel {

    void start(int port, Executor executor, Protocol protocol, ServerMessageHandler messageHandler) throws IOException;


}
