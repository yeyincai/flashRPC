/**
 * @filename:MessageCallBack.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:Rpc消息回调
 * @author tangjie
 * @version 1.0
 *
 */
package com.flashrpc.core.client;


import com.flashrpc.core.metadata.RpcResponse;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MessageCallBack {

    private RpcResponse response;
    private Lock lock = new ReentrantLock();
    private Condition finish = lock.newCondition();

    public Object start() throws InterruptedException {
        try {
            lock.lock();
            finish.await(10*1000, TimeUnit.MILLISECONDS);
            if (this.response != null) {
                if(this.response.getError()!=null){
                    throw new RuntimeException(response.getError());
                }else {
                    return this.response.getResult();
                }
            } else {
                return null;
            }
        } finally {
            lock.unlock();
        }
    }

    public void over(RpcResponse response) {
        try {
            lock.lock();
            finish.signal();
            this.response = response;
        } finally {
            lock.unlock();
        }
    }
}
