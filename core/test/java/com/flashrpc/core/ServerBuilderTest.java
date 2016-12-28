package com.flashrpc.core;

import org.junit.Test;

/**
 * Created by yeyc on 2016/12/28.
 */
public class ServerBuilderTest {

    @Test
    public void test(){
        Server server = ServerBuilder.forPort(8888).addService(String.class).build();
        server.start();
    }
}
