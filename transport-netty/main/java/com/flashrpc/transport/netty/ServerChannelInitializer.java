package com.flashrpc.transport.netty;


import com.flashrpc.core.MessageHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;


public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger logger = LoggerFactory.getLogger(ServerChannelInitializer.class);

    private Executor executor;
    private MessageHandler messageHandler;

    public ServerChannelInitializer(Executor executor, MessageHandler messageHandler) {
        this.executor = executor;
        this.messageHandler = messageHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("server-message-handler", new ServerMessageHandlerImpl(executor, messageHandler));
        logger.info("ServerChannelInitializer  initChannel.....");
    }
}
