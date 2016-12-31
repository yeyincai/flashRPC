package com.flashrpc.transport.netty.client;


import com.flashrpc.core.exceptions.FlashRPCException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

public class ClientChannelBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ClientChannelBuilder.class);
    private final static int TIME_OUT = 5000;

    public static Channel build(SocketAddress socketAddress,EventLoopGroup workerGroup, ChannelHandler channelHandler) {
        Bootstrap b1 = new Bootstrap();
        Channel channel = b1.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(channelHandler).connect(socketAddress).channel();

        long startTime = System.currentTimeMillis();
        while (true) {
            if (channel.isActive()) {
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                logger.error("channel is no init socketAddress={}", socketAddress);
                throw new FlashRPCException("channel is no init socketAddress", e);
            }
            if ((System.currentTimeMillis() - startTime) > TIME_OUT) {
                logger.error("channel  connection time out!  socketAddress={}", socketAddress);
                throw new FlashRPCException("channel  connection time out!");
            }
        }

        return channel;
    }

}
