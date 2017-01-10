package com.flashrpc.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 解决tcp粘包的问题
 * Created by yeyc on 2016/12/31.
 */
public class MessageCodec extends ByteToMessageCodec<byte[]> {

    private static final Logger logger = LoggerFactory.getLogger(MessageCodec.class);

    final static int MESSAGE_LENGTH = 4;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, byte[] msg, ByteBuf byteBuf) throws Exception {
        int dataLength = msg.length;
        byteBuf.writeInt(dataLength);
        byteBuf.writeBytes(msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < MessageCodec.MESSAGE_LENGTH) {
            return;
        }

        in.markReaderIndex();
        int messageLength = in.readInt();

        if (messageLength < 0) {
            ctx.close();
        }

        if (in.readableBytes() < messageLength) {
            in.resetReaderIndex();
        } else {
            byte[] messageBody = new byte[messageLength];
            in.readBytes(messageBody);
            out.add(messageBody);
        }
    }


}
