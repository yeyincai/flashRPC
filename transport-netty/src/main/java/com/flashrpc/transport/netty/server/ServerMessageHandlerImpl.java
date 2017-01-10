package com.flashrpc.transport.netty.server;

import com.flashrpc.core.SendMessage;
import com.flashrpc.core.server.ServerMessageHandler;
import com.flashrpc.transport.netty.util.ChannelWriteMessageUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * Created by yeyc on 2016/12/30.
 */
public class ServerMessageHandlerImpl extends ChannelInboundHandlerAdapter implements SendMessage {

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
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        logger.info("service read msg={} ",msg);

        //接收客户端发送的数据
        if(!(msg instanceof byte[])){
            ReferenceCountUtil.release(msg);
            return;
        }
        //接收服务端发送的数据
        final byte[] writeMsg = (byte[]) msg;
        //任务异步化
        executor.execute(() -> messageHandler.receiveAndProcessor(writeMsg,this));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("MessageHandler异常", cause);
        if (outboundChannel.isActive()) {
            outboundChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }


    @Override
    public void sendMsg(byte[] msg) {
        ChannelWriteMessageUtil.sendMsg(outboundChannel,msg);
    }
}
