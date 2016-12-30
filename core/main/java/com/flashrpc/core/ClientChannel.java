package com.flashrpc.core;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by yeyc on 2016/12/28.
 */
public interface ClientChannel {

    void start(InetSocketAddress socketAddress) throws IOException;

    void shutdown();
}
