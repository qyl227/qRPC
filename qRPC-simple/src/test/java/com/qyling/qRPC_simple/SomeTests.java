package com.qyling.qRPC_simple;

import com.qyling.qRPC_simple.serialize.RpcSerializer;

/**
 * @author qyling
 * @date 2024/10/31 7:34
 */
public class SomeTests {
    public static void main(String[] args) {
        byte[] bytes = RpcSerializer.serialize(new String("324"));
        Object obj = RpcSerializer.deserialize(bytes, Object.class);
        System.out.println("obj = " + obj);
    }
}
