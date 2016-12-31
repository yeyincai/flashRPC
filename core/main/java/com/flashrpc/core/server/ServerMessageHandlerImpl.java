package com.flashrpc.core.server;

import com.flashrpc.core.SendMessage;
import com.flashrpc.core.Serializer;
import com.flashrpc.core.metadata.RpcRequest;
import com.flashrpc.core.metadata.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by yeyc on 2016/12/30.
 */
public class ServerMessageHandlerImpl implements ServerMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerMessageHandlerImpl.class);
    private Serializer serializer;
    private Object serviceBean;
    private ServerChannel channel;

    public ServerMessageHandlerImpl(Object serviceBean , Serializer serializer, ServerChannel channel) {
        this.serviceBean = serviceBean;
        this.serializer = serializer;
        this.channel = channel;
    }

    @Override
    public void receiveAndProcessor(byte[] request,SendMessage receiveMessage) {
        RpcRequest rpcRequest = serializer.deserializer(request, RpcRequest.class);

        /*if (!serviceClass.getSimpleName().equals(rpcRequest.getClassName())) {
            throw new FlashRPCException("client send msg fail");
        }*/

        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());

        Method method = null;
        try {
            method = serviceBean.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            //返回数据给客户端
        } catch (Exception e) {
            logger.error("client send msg is fail", e);
            rpcResponse.setError(e);
        }

        if (method != null) {
            try {
                rpcResponse.setResult(method.invoke(serviceBean, rpcRequest.getParameters()));
            } catch (Exception e) {
                logger.error("method ={}   invoke  fail", method.getName(), e);
                rpcResponse.setError(e);
            }
        }

        //发送数据给客户端
        receiveMessage.sendMsg(serializer.serializer(rpcResponse));
    }


}
