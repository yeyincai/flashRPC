package com.flashrpc.protocol.http2;

import com.flashrpc.core.Protocol;
import io.netty.handler.codec.http2.Http2FrameCodec;

/**
 * Created by yeyc on 2016/12/30.
 */
public class Http2Protocol extends Http2FrameCodec implements Protocol{

    public Http2Protocol(boolean server) {
        super(server);
    }
}
