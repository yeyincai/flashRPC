package com.flashrpc.core.client;

import com.flashrpc.core.Serializer;
import com.flashrpc.core.metadata.RpcRequest;
import com.flashrpc.core.metadata.RpcResponse;
import com.flashrpc.core.server.ServerMessageHandlerImpl;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by yeyc on 2016/12/31.
 */
class ClientMessageHandlerImpl implements ClientMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerMessageHandlerImpl.class);

    private Serializer serializer;
    private ClientChannel channel;
    private static final int TIME_AWAIT = 30 * 1000;

    private Map<Long, BlockingQueue<RpcResponse>> mapCallBack;

    ClientMessageHandlerImpl(Serializer serializer, ClientChannel channel) {
        this.serializer = serializer;
        this.channel = channel;
        mapCallBack = Maps.newConcurrentMap();
    }

    @Override
    public void receiveAndProcessor(byte[] request) {
        final RpcResponse rpcResponse = this.serializer.deserializer(request, RpcResponse.class);
        if (rpcResponse != null && rpcResponse.getRequestId() > 0) {
            BlockingQueue<RpcResponse> queue = mapCallBack.get(rpcResponse.getRequestId());
            queue.add(rpcResponse);
            mapCallBack.remove(rpcResponse.getRequestId());
        } else {
            logger.error("receiveAndProcessor   not found data   getRequestId={}", rpcResponse.getRequestId());
        }
    }


    @Override
    public Object sendAndProcessor(RpcRequest rpcRequest) throws InterruptedException {
        final byte[] requestMsg = this.serializer.serializer(rpcRequest);

        final BlockingQueue<RpcResponse> queue = new LinkedBlockingQueue<>();
        mapCallBack.put(rpcRequest.getRequestId(), queue);
        channel.sendMsg(requestMsg);

        RpcResponse response = queue.poll(TIME_AWAIT, TimeUnit.MILLISECONDS);
        if (response != null) {
            if (response.getError() != null) {
                throw new RuntimeException(response.getError());
            } else {
                return response.getResult();
            }
        } else {
            throw new RuntimeException("request wait response time await!");
        }
    }


}
