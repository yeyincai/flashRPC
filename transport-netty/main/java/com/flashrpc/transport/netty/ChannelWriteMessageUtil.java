package com.flashrpc.transport.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yeyc on 2016/12/30.
 */
public class ChannelWriteMessageUtil {

    private static final Logger logger = LoggerFactory.getLogger(ChannelWriteMessageUtil.class);

    static  void  sendMsg(Channel outboundChannel, Object obj) {
        outboundChannel.writeAndFlush(obj).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {
                    outboundChannel.read();
                } else {
                    logger.error("outboundChannel={}, sendMsg={}", outboundChannel, obj, future.cause());
                }
            }
        });
    }
}
