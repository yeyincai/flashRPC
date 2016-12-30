package com.flashrpc.transport.netty;

import com.flashrpc.core.ClientChannel;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by yeyc on 2016/12/29.
 */
public class NettyClientChannel implements ClientChannel {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerChannel.class);
    private EventLoopGroup workerGroup;
    private Channel channel;

    @Override
    public void start(InetSocketAddress socketAddress) throws IOException {
        workerGroup = new NioEventLoopGroup();
        channel = ClientChannelBuilder.build(socketAddress, workerGroup, new ClientChannelInitializer());
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
            workerGroup.shutdownGracefully();
        }
    }

}
