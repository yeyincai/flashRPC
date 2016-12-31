package com.flashrpc.transport.netty.client;

import com.flashrpc.core.client.ClientMessageHandler;
import com.flashrpc.transport.netty.util.ChannelWriteMessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yeyc on 2016/12/31.
 */
public class ClientMessageHandlerImpl extends ChannelDuplexHandler {

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
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        logger.info("client write  msg={}",msg);
        if(!(msg instanceof byte[])){
            return;
        }
        ByteBuf byteBuf = ctx.alloc().buffer().writeBytes((byte[]) msg);
        ChannelWriteMessageUtil.sendMsg(outboundChannel,byteBuf);
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        logger.info("client read  msg={}",msg);

        if(!(msg instanceof ByteBuf)){
            return;
        }
        //接收服务端发送的数据
        final ByteBuf writeMsg = (ByteBuf) msg;
        final byte[] request = new byte[writeMsg.readableBytes()];
        writeMsg.readBytes(request);

        messageHandler.receiveAndProcessor(request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("MessageHandler异常", cause);
        if (outboundChannel.isActive()) {
            outboundChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

}
