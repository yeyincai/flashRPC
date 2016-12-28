package com.flashrpc.core.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by yeyc on 2016/12/28.
 */
public class ExecutorBuilder {

    public static Executor executorBuild(String nameFormat, boolean daemon) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(daemon).setNameFormat(nameFormat).build();
        return Executors.newCachedThreadPool(threadFactory);
    }
}
