package com.qyling.qRPC_simple.serialize;

import com.qyling.qRPC_simple.config.ConfigUtils;

/**
 * RPC的序列化器
 * @author qyling
 * @date 2024/10/31 7:31
 */
public class RpcSerializer {
    /**
     * 根据配置加载类加载器，默认为JDK类加载器
     */
    private static Serializer serializer = ConfigUtils.getConfig().getSerializerObj();


    /**
     * 序列化对象为byte[]
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        if (serializer == null) {
            throw new IllegalStateException("序列化器未指定");
        }
        return serializer.serialize(object);
    }

    /**
     * 序列化byte[]为对象
     * @param bytes
     * @param clazz 目标对象类型
     * @return
     * @param <T>
     */
    public static  <T> T deserialize(byte[] bytes, Class<T> clazz){
        if (serializer == null) {
            throw new IllegalStateException("序列化器未指定");
        }
        return serializer.deserialize(bytes, clazz);
    }
}
