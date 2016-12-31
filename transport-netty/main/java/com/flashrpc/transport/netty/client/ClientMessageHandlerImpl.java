package com.flashrpc.transport.netty.client;

import com.flashrpc.core.client.ClientMessageHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yeyc on 2016/12/31.
 */
public class ClientMessageHandlerImpl extends ChannelInboundHandlerAdapter {

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

        if(!(msg instanceof ByteBuf)){
            ReferenceCountUtil.release(msg);
            return;
        }
        //接收服务端发送的数据
        final ByteBuf writeMsg = (ByteBuf) msg;
        final byte[] request = new byte[writeMsg.readableBytes()];
        writeMsg.readBytes(request);

        messageHandler.receiveAndProcessor(request);
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
