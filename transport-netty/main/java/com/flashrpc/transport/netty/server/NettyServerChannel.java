package com.flashrpc.transport.netty.server;

import com.flashrpc.core.Protocol;
import com.flashrpc.core.server.ServerChannel;
import com.flashrpc.core.server.ServerMessageHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Created by yeyc on 2016/12/29.
 */
public class NettyServerChannel implements ServerChannel {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerChannel.class);
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;

    @Override
    public void start(int port, Executor executor, Protocol protocol, ServerMessageHandler messageHandler) throws IOException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ChannelFuture nettyServerChannelFuture = ServerChannelBuilder.build(bossGroup, workerGroup, new ServerChannelInitializer(executor,messageHandler), port);

        try {
            nettyServerChannelFuture.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            logger.error("启动失败...", ex);
            throw new RuntimeException("Interrupted waiting for bind");
        }
        if (!nettyServerChannelFuture.isSuccess()) {
            logger.error("启动失败...", nettyServerChannelFuture.cause());
            throw new IOException("Failed to bind", nettyServerChannelFuture.cause());
        }
        channel = nettyServerChannelFuture.channel();
    }

    @Override
    public void shutdown() {
        if(channel==null || !channel.isOpen()){
            return;
        }

        try {
            channel.close();
        } catch (Exception e) {
            logger.error("close NettyServerChannel fail ",e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    @Override
    public void sendMsg(byte[]  msg){
        channel.write(msg);
    }
}
