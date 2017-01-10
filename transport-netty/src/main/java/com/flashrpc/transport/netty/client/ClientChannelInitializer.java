package com.flashrpc.transport.netty.client;

import com.flashrpc.core.client.ClientMessageHandler;
import com.flashrpc.transport.netty.MessageCodec;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yeyc on 2016/12/20.
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger logger = LoggerFactory.getLogger(ClientChannelInitializer.class);

    private ClientMessageHandler clientMessageHandler;
    public ClientChannelInitializer(ClientMessageHandler clientMessageHandler){
        this.clientMessageHandler = clientMessageHandler;
    }
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("log",new LoggingHandler(LogLevel.DEBUG));
        ch.pipeline().addLast("messageCodec", new MessageCodec());
        ch.pipeline().addLast("clientMessageHandlerImpl", new ClientMessageHandlerImpl(clientMessageHandler));
        logger.info("ClientChannelInitializer  initChannel.....");
    }
}
