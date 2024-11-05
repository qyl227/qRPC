package com.qyling.qRPC_simple;

import com.qyling.qRPC_simple.serialize.Serializer;

import java.util.ServiceLoader;

/**
 * @author qyling
 * @date 2024/11/5 13:27
 */
public class SpiLoadTests {
    public static void main(String[] args) {
        ServiceLoader<Serializer> serviceLoader = ServiceLoader.load(Serializer.class);

        serviceLoader.forEach(serializer -> System.out.println(serializer.getClass()));
    }
}
