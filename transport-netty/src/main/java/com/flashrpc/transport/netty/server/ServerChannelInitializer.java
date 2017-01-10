package com.flashrpc.transport.netty.server;


import com.flashrpc.core.server.ServerMessageHandler;
import com.flashrpc.transport.netty.MessageCodec;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;


public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger logger = LoggerFactory.getLogger(ServerChannelInitializer.class);

    private Executor executor;
    private ServerMessageHandler messageHandler;

    public ServerChannelInitializer(Executor executor, ServerMessageHandler messageHandler) {
        this.executor = executor;
        this.messageHandler = messageHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("log",new LoggingHandler(LogLevel.DEBUG));
        pipeline.addLast("messageCodec", new MessageCodec());
        pipeline.addLast("server-message-handler", new ServerMessageHandlerImpl(executor, messageHandler));

        logger.info("ServerChannelInitializer  initChannel.....");
    }
}
