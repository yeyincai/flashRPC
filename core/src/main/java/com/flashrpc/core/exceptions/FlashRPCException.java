package com.flashrpc.core.exceptions;

/**
 * Created by yeyc on 2016/12/28.
 */
public class FlashRPCException extends RuntimeException {

    public FlashRPCException(String message) {
        super(message);
    }

    public FlashRPCException(String message, Throwable t) {
        super(message, t);
    }

}