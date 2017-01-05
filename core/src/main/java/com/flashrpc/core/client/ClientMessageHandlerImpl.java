package com.flashrpc.core.client;

import com.flashrpc.core.Serializer;
import com.flashrpc.core.exceptions.FlashRPCException;
import com.flashrpc.core.metadata.RpcRequest;
import com.flashrpc.core.metadata.RpcResponse;
import com.flashrpc.core.server.ServerMessageHandlerImpl;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/**
 * Created by yeyc on 2016/12/31.
 */
public class ClientMessageHandlerImpl implements ClientMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerMessageHandlerImpl.class);

    private Serializer serializer;
    private ClientChannel channel;

    private Map<Long, MessageObserver> mapCallBack;
    private ExecutorService executor = Executors.newCachedThreadPool();

    ClientMessageHandlerImpl(Serializer serializer, ClientChannel channel) {
        this.serializer = serializer;
        this.channel = channel;
        mapCallBack = Maps.newConcurrentMap();
    }

    @Override
    public void receiveAndProcessor(byte[] request) {
        final RpcResponse rpcResponse = this.serializer.deserializer(request, RpcResponse.class);
        if (rpcResponse != null && rpcResponse.getRequestId() > 0) {
            MessageObserver messageObserver = mapCallBack.get(rpcResponse.getRequestId());
            if (messageObserver != null) {
                messageObserver.over(rpcResponse);
                mapCallBack.remove(rpcResponse.getRequestId());
            } else {
                logger.error("mapCallBack   not found data   getRequestId={}",rpcResponse.getRequestId());
            }
        }
        else {
            logger.error("receive server data fail data={}",rpcResponse);
        }
    }

    @Override
    public Object sendAndProcessor(RpcRequest rpcRequest) throws InterruptedException {
        final byte[] requestMsg = this.serializer.serializer(rpcRequest);
        final MessageObserver messageCallBack = new MessageObserver();
        mapCallBack.put(rpcRequest.getRequestId(), messageCallBack);

        Future<?> submit = executor.submit(() -> messageCallBack.start());

        channel.sendMsg(requestMsg);
        try {
            return submit.get();
        }catch (ExecutionException e) {
            throw  new FlashRPCException("request fail",e);
        }
    }


}
