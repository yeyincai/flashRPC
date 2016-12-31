package com.flashrpc.transport.netty.client;


import com.flashrpc.core.exceptions.FlashRPCException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class ClientChannelBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ClientChannelBuilder.class);

    public static Channel build(SocketAddress socketAddress, EventLoopGroup workerGroup, ChannelHandler channelHandler) {
        Bootstrap b1 = new Bootstrap();
        ChannelFuture channelFuture;
        try {
            channelFuture = b1.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(channelHandler).connect(socketAddress).sync().await();
        } catch (InterruptedException e) {
            logger.error("channel  connection time out!  socketAddress={}", socketAddress);
            throw new FlashRPCException("channel  connection time out!");
        }

        return channelFuture.channel();

    }

}
