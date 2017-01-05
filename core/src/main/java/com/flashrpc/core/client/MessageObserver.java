/**
 * @filename:MessageCallBack.java Newland Co. Ltd. All rights reserved.
 * @Description:Rpc消息回调
 * @author tangjie
 * @version 1.0
 */
package com.flashrpc.core.client;


import com.flashrpc.core.metadata.RpcResponse;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class MessageObserver {

    private final BlockingQueue<RpcResponse> queue = new LinkedBlockingQueue<>();

    Object start() throws InterruptedException {
        RpcResponse response = queue.poll(30 * 1000, TimeUnit.MILLISECONDS);
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

    void over(RpcResponse response) {
        queue.add(response);
    }


}
