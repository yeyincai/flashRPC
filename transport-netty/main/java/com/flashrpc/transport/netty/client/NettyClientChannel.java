package com.flashrpc.transport.netty.client;

import com.flashrpc.core.Protocol;
import com.flashrpc.core.client.ClientChannel;
import com.flashrpc.core.client.ClientMessageHandler;
import com.flashrpc.transport.netty.server.NettyServerChannel;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;

/**
 * Created by yeyc on 2016/12/29.
 */
public class NettyClientChannel implements ClientChannel {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerChannel.class);
    private EventLoopGroup workerGroup;
    private Channel channel;
    private Protocol protocol;

    @Override
    public void start(ClientMessageHandler messageHandler, SocketAddress socketAddress, Protocol protocol) throws IOException {
        this.protocol = protocol;
        workerGroup = new NioEventLoopGroup();
        channel = ClientChannelBuilder.build(socketAddress, workerGroup, new ClientChannelInitializer(messageHandler));
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

    @Override
    public void sendMsg(byte[] msg) {
        channel.write(msg);
    }


}
