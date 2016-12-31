package com.flashrpc.core.server;

import com.flashrpc.core.Protocol;
import com.flashrpc.core.Serializer;
import com.flashrpc.core.exceptions.FlashRPCException;
import com.flashrpc.core.util.ExecutorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Created by yeyc on 2016/12/28.
 */
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private ServerChannel serverChannel;
    private Serializer serializer;//序列化
    private Protocol protocol;//传输协议
    private int port;//服务的d端口
    private Object serviceBean;//需要发布rpc的服务,一条channel可以发布服务，但是这里不采用这种方案，一条channel对应一个服务

    private ServerMessageHandler messageHandler;
    private Executor executor;

    Server(ServerChannel serverChannel, Serializer serializer, Protocol protocol, int port, Object serviceBean) {
        this.executor = ExecutorBuilder.executorBuild("flashrpc-business-executor-%d", true);
        this.serviceBean = serviceBean;
        this.serverChannel = serverChannel;
        this.serializer = serializer;
        this.port = port;
        this.protocol = protocol;

        messageHandler = new ServerMessageHandlerImpl(serviceBean,serializer,serverChannel);
    }

    public void start() {
        try {
            serverChannel.start(port,executor,protocol,messageHandler);
            logger.info("server start port={}",port);
        } catch (IOException e) {
            throw  new FlashRPCException("serverChannel init fail",e);
        }
    }

    public void shutdown() {
        serverChannel.shutdown();
    }
}
