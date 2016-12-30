package com.flashrpc.core.impl;

import com.flashrpc.core.MessageHandler;
import com.flashrpc.core.Serializer;
import com.flashrpc.core.ServerChannel;
import com.flashrpc.core.exceptions.FlashRPCException;
import com.flashrpc.core.metadata.RpcRequest;
import com.flashrpc.core.metadata.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by yeyc on 2016/12/30.
 */
public class MessageHandlerImpl implements MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerImpl.class);
    private Serializer serializer;
    private Class serviceClass;
    private ServerChannel serverChannel;

    public MessageHandlerImpl(Class serviceClass, Serializer serializer,ServerChannel serverChannel) {
        this.serviceClass = serviceClass;
        this.serializer = serializer;
        this.serverChannel = serverChannel;
    }

    @Override
    public void readAndProcessor(byte[] request) {
        RpcRequest rpcRequest = serializer.deserializer(request, RpcRequest.class);

        if (!serviceClass.getSimpleName().equals(rpcRequest.getClassName())) {
            throw new FlashRPCException("client send msg fail");
        }

        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());

        Method method = null;
        try {
            method = serviceClass.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            //返回数据给客户端
        } catch (Exception e) {
            logger.error("client send msg is fail", e);
            rpcResponse.setError(e);
        }

        if (method != null) {
            try {
                rpcResponse.setResult(method.invoke(serviceClass, rpcRequest.getParameters()));
            } catch (Exception e) {
                logger.error("method ={}   invoke  fail", method.getName(), e);
                rpcResponse.setError(e);
            }
        }

        //发送数据给客户端
        serverChannel.sendMsg(serializer.serializer(rpcResponse));
    }
}
