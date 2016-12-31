package com.flashrpc.transport.netty.server;

import com.flashrpc.core.server.ServerMessageHandler;
import com.flashrpc.transport.netty.util.ChannelWriteMessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * Created by yeyc on 2016/12/30.
 */
public class ServerMessageHandlerImpl extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerMessageHandlerImpl.class);

    private Channel outboundChannel;
    private ServerMessageHandler messageHandler;
    private Executor executor;

    public ServerMessageHandlerImpl(Executor executor, ServerMessageHandler messageHandler) {
        this.executor = executor;
        this.messageHandler = messageHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        outboundChannel = ctx.channel();
        logger.info("ServerMessageHandlerImpl  init ");
        ctx.read();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        logger.info("service write msg={} ",msg);
        if(!(msg instanceof byte[])){
            return;
        }
        ByteBuf byteBuf = ctx.alloc().buffer().writeBytes((byte[]) msg);
        ChannelWriteMessageUtil.sendMsg(outboundChannel,byteBuf);
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        logger.info("service read msg={} ",msg);
        if(!(msg instanceof ByteBuf)){
            return;
        }
        //接收客户端发送的数据
        final ByteBuf writeMsg = (ByteBuf) msg;
        final byte[] request = new byte[writeMsg.readableBytes()];
        writeMsg.readBytes(request);

        //任务异步化
        executor.execute(() -> messageHandler.receiveAndProcessor(request));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("MessageHandler异常", cause);
        if (outboundChannel.isActive()) {
            outboundChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

}
