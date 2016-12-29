package com.flashrpc.transport.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http2.Http2FrameCodec;

/**
 * Created by yeyc on 2016/12/20.
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("http2FrameCodec", new Http2FrameCodec(false));

    }
}
