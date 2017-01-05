package com.flashrpc.serialize.protostuff;



/**
 * Created by yeyc on 2017/1/4.
 */
public class ProtostuffCodecCurrentTest {

    public static void main(String[] args) {
        SimpleExecutor simpleExecutor = new SimpleExecutor(()->{
            new ProtostuffCodecUtilTest().test();
        });

        simpleExecutor.execute(20,30);
    }
}
