package com.flashrpc.transport.netty;

import com.flashrpc.core.ServerChannel;
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
    private Executor executor;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelFuture nettyServerChannelFuture;

    @Override
    public void start(int port, Executor executor) throws IOException {
        this.executor = executor;
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        nettyServerChannelFuture = ServerChannelBuilder.build(bossGroup, workerGroup, new ServerChannelInitializer(), port);

        try {
            nettyServerChannelFuture.sync();
        } catch (InterruptedException e) {
            logger.error("中断异常", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("启动失败...", e);
            System.exit(-1);
        }
    }

    @Override
    public void shutdown() {
        try {
            nettyServerChannelFuture.channel().close();
        } catch (Exception e) {
            logger.error("close NettyServerChannel fail ",e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
