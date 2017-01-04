package com.flashrpc.core.client;

import com.flashrpc.core.AbstractChannel;
import com.flashrpc.core.Protocol;

import java.io.IOException;
import java.net.SocketAddress;

/**
 * Created by yeyc on 2016/12/28.
 */
public interface ClientChannel extends AbstractChannel {

    void start(ClientMessageHandler messageHandler, SocketAddress socketAddress, Protocol protocol ) throws IOException;

    void sendMsg(byte[]  msg);
}
