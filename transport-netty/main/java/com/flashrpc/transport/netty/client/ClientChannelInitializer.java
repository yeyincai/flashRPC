package com.flashrpc.transport.netty.client;

import com.flashrpc.core.client.ClientMessageHandler;
import com.flashrpc.transport.netty.MessageDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by yeyc on 2016/12/20.
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private ClientMessageHandler clientMessageHandler;
    public ClientChannelInitializer(ClientMessageHandler clientMessageHandler){
        this.clientMessageHandler = clientMessageHandler;
    }
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("log",new LoggingHandler(LogLevel.DEBUG));
        ch.pipeline().addLast("messageDecoder",new MessageDecoder());
        ch.pipeline().addLast("clientMessageHandlerImpl", new ClientMessageHandlerImpl(clientMessageHandler));

    }
}
