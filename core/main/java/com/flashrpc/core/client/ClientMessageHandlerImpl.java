package com.flashrpc.core.client;

import com.flashrpc.core.Serializer;
import com.flashrpc.core.metadata.RpcRequest;
import com.flashrpc.core.metadata.RpcResponse;
import com.flashrpc.core.server.ServerMessageHandlerImpl;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by yeyc on 2016/12/31.
 */
public class ClientMessageHandlerImpl implements ClientMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerMessageHandlerImpl.class);

    private Serializer serializer;
    private ClientChannel channel;

    private Map<Long, MessageCallBack> mapCallBack;


    protected ClientMessageHandlerImpl(Serializer serializer, ClientChannel channel) {
        this.serializer = serializer;
        this.channel = channel;
        mapCallBack = Maps.newConcurrentMap();
    }

    @Override
    public void receiveAndProcessor(byte[] request) {
        final RpcResponse rpcResponse = this.serializer.deserializer(request, RpcResponse.class);
        if (rpcResponse != null && rpcResponse.getRequestId() > 0) {

            MessageCallBack messageCallBack = mapCallBack.get(rpcResponse.getRequestId());
            if (messageCallBack != null) {
                messageCallBack.over(rpcResponse);
                mapCallBack.remove(rpcResponse.getRequestId());
            }
        }
    }

    @Override
    public MessageCallBack sendAndProcessor(RpcRequest rpcRequest) {
        final byte[] requestMsg = this.serializer.serializer(rpcRequest);
        channel.sendMsg(requestMsg);
        final MessageCallBack messageCallBack = new MessageCallBack();
        mapCallBack.put(rpcRequest.getRequestId(), messageCallBack);
        return messageCallBack;
    }


}
