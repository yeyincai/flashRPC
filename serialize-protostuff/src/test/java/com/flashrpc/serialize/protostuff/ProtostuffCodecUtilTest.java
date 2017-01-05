package com.flashrpc.serialize.protostuff;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class ProtostuffCodecUtilTest {

    @Test
    public void test(){
        Pojo pojo = new Pojo(18,"yyc");
        ProtostuffSerializer protostuffCodec = new ProtostuffSerializer();
        byte[] serializer = protostuffCodec.serializer(pojo);
        Pojo deserializer = protostuffCodec.deserializer(serializer, Pojo.class);
        assertThat(deserializer.getAge()).isEqualTo(pojo.getAge());
        assertThat(deserializer.getName()).isEqualTo(pojo.getName());
    }

}
