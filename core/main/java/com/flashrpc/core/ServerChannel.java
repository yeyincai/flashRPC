package com.flashrpc.core;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Created by yeyc on 2016/12/28.
 */
public interface ServerChannel {

    void start(int port,Executor executor,Protocol protocol,MessageHandler messageHandler) throws IOException;

    void shutdown();

    void sendMsg(byte[]  msg);
}
