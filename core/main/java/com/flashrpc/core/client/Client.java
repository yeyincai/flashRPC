package com.flashrpc.core.client;

import com.flashrpc.core.Protocol;
import com.flashrpc.core.Serializer;
import com.flashrpc.core.exceptions.FlashRPCException;
import com.flashrpc.core.metadata.RpcRequest;
import com.flashrpc.core.util.ServiceLoadUtil;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * Created by yeyc on 2016/12/31.
 */
class Client<Builder extends Client, T> {

    private ClientChannel clientChannel;
    private Serializer serializer;//序列化
    private Protocol protocol;//传输协议
    private SocketAddress socketAddress;
    private AtomicLong atomicLong;
    private ClientMessageHandler messageHandler;
    private final Class<T> clientClass;

    Client(Class<T> clientClass) {
        this.clientClass = clientClass;
        this.serializer = ServiceLoadUtil.getProvider(Serializer.class);
        this.clientChannel = ServiceLoadUtil.getProvider(ClientChannel.class);
        this.atomicLong = new AtomicLong(0);
        //this.protocol = ServiceLoadUtil.getProvider(Protocol.class);
    }

    public Builder forAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
        return (Builder) this;
    }

    public Builder forAddress(String host, int port) {
        Preconditions.checkNotNull(host);
        Preconditions.checkArgument(port > 0, "port is > 0!");
        this.socketAddress = InetSocketAddress.createUnresolved(host, port);
        return (Builder) this;
    }


    void start() {
        try {
            clientChannel.start(messageHandler, socketAddress, protocol);
            messageHandler =  new ClientMessageHandlerImpl(serializer,clientChannel);
        } catch (IOException e) {
            throw new FlashRPCException("clientChannel init fail", e);
        }
    }

    T getClientProxy() {
        final InvocationHandler clientInvocationHandler = (proxy, method, args) -> {
            RpcRequest request = new RpcRequest();
            request.setRequestId(atomicLong.incrementAndGet());
            request.setClassName(clientClass.getName());
            request.setMethodName(method.getName());
            request.setParameterTypes(method.getParameterTypes());
            request.setParameters(args);

            MessageCallBack callBack = messageHandler.sendAndProcessor(request);
            return callBack.start();
        };

        return (T) newProxyInstance(Client.class.getClassLoader(), new Class[]{clientClass}, clientInvocationHandler);
    }

    @Override
    protected void finalize() throws Throwable {
        clientChannel.shutdown();
        super.finalize();
    }
}
