package com.flashrpc.transport.netty.client;

import com.flashrpc.core.client.ClientMessageHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yeyc on 2016/12/31.
 */
public class ClientMessageHandlerImpl extends ChannelInboundHandlerAdapter{

    private static final Logger logger = LoggerFactory.getLogger(ClientMessageHandlerImpl.class);

    private Channel outboundChannel;
    private ClientMessageHandler messageHandler;

    public ClientMessageHandlerImpl( ClientMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        outboundChannel = ctx.channel();
        logger.info("ClientMessageHandlerImpl  init ");
        ctx.read();
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        logger.info("client read  msg={}",msg);

        if(!(msg instanceof byte[])){
            ReferenceCountUtil.release(msg);
            return;
        }
        //接收服务端发送的数据
        final byte[] writeMsg = (byte[]) msg;

        messageHandler.receiveAndProcessor(writeMsg);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("MessageHandler异常", cause);
        if (outboundChannel.isActive()) {
            outboundChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

}
