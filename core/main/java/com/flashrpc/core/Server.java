package com.flashrpc.core;

import com.flashrpc.core.impl.MessageHandlerImpl;
import com.flashrpc.core.util.ExecutorBuilder;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Created by yeyc on 2016/12/28.
 */
public class Server {
    private ServerChannel serverChannel;
    private Serializer serializer;//序列化
    private Protocol protocol;//传输协议
    private int port;//服务的d端口
    private Class serviceClass;//需要发布rpc的服务,一条channel可以发布服务，但是这里不采用这种方案，一条channel对应一个服务

    private MessageHandler messageHandler;
    private Executor executor;



    Server(ServerChannel serverChannel, Serializer serializer, Protocol protocol, int port, Class serviceClass) {
        this.executor = ExecutorBuilder.executorBuild("flashrpc-business-executor-%d", true);
        this.serviceClass = serviceClass;
        this.serverChannel = serverChannel;
        this.serializer = serializer;
        this.port = port;
        this.protocol = protocol;

        messageHandler = new MessageHandlerImpl(serviceClass,serializer);
    }

    public void start() {
        try {
            serverChannel.start(port,executor,protocol,messageHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        serverChannel.shutdown();
    }
}
