package com.flashrpc.transport.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by xuruquan on 2016/12/7.
 */
public class ServerChannelBuilder {

    public static ChannelFuture build(EventLoopGroup bossGroup,EventLoopGroup workerGroup, ChannelHandler channelHandler, int port) {

        ServerBootstrap b = new ServerBootstrap();
        return b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(channelHandler)
                .childOption(ChannelOption.AUTO_READ, false)
                .bind(port);
    }
}
