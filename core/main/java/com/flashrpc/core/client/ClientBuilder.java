package com.flashrpc.core.client;

/**
 * Created by yeyc on 2016/12/28.
 */
public final class ClientBuilder<T> extends Client<ClientBuilder<T>, T>{

    private ClientBuilder(Class<T> clientClass) {
        super(clientClass);
    }

    public static <T> ClientBuilder<T>  builderClass(Class<T> interfaceClass) {
        return new ClientBuilder(interfaceClass);
    }

    public T build() {
        super.start();
        //创建代理类
        return super.getClientProxy();
    }

}
