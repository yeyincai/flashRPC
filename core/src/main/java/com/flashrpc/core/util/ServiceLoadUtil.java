package com.flashrpc.core.util;

import com.flashrpc.core.exceptions.FlashRPCException;

import java.util.ServiceLoader;


public class ServiceLoadUtil {

    public static <T> T getProvider(Class<T> type){
        T result = null;
        for (T service : ServiceLoader.load(type)) {
            if(result!=null){
                throw new FlashRPCException(type.getSimpleName() + " is not allow multiple !");
            }
            result = service;
        }
        if(result!=null){
            return result;
        }
        throw new FlashRPCException(type.getSimpleName() + " not be found");
    }
}
