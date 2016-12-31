package com.flashrpc.serialize.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.flashrpc.core.Serializer;
import com.flashrpc.core.exceptions.FlashRPCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.dyuproject.protostuff.runtime.RuntimeSchema.getSchema;
import static com.flashrpc.serialize.protostuff.SchemaCache.getInstance;


public class ProtostuffSerializer implements Serializer {
    private static final Logger logger = LoggerFactory.getLogger(ProtostuffSerializer.class);

    private Lock lock = new ReentrantLock();

    public <T> byte[] serializer(T obj) {
        @SuppressWarnings("unchecked")
        final Lock tlock = this.lock;
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            tlock.lock();
            Schema<T> schema = (Schema<T>) getInstance().get(obj.getClass());
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            logger.error("Protostuff serializer fail  obj={}", obj, e);
            throw new FlashRPCException("Protostuff serializer fail", e);
        } finally {
            buffer.clear();
            tlock.unlock();
        }


    }

    public <T> T deserializer(byte[] data, Class<T> clazz) {
        final Lock tlock = this.lock;
        try {
            tlock.lock();
            T obj = clazz.newInstance();
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(data, obj, schema);
            return obj;
        } catch (Exception e) {
            logger.error("Protostuff deserializer fail  data={}", data, e);
            throw new FlashRPCException("Protostuff deserializer fail", e);
        }finally {
            tlock.unlock();
        }
    }
}
