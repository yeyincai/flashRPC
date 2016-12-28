package com.flashrpc.core;

import com.flashrpc.core.util.ExecutorBuilder;

import java.util.concurrent.Executor;

/**
 * Created by yeyc on 2016/12/28.
 */
public class Server {
    private ServerChannel serverChannel;
    private Serializer serializer;//序列化
    private Protocol protocol;//传输协议
    private int port;//服务的d端口
    private Class serviceClass;//需要发布rpc的服务

    private Executor executor;

    Server(ServerChannel serverChannel, Serializer serializer, Protocol protocol, int port, Class serviceClass) {
        this.executor = ExecutorBuilder.executorBuild("flashrpc-business-executor-%d", true);
        this.serviceClass = serviceClass;
        this.serverChannel = serverChannel;
        this.serializer = serializer;
        this.port = port;
        this.protocol = protocol;
    }

    public void start() {
    }

    public void stop() {
    }
}
