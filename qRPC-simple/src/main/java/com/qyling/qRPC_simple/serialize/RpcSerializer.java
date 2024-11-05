package com.qyling.qRPC_simple.serialize;

/**
 * RPC的序列化器
 * @author qyling
 * @date 2024/10/31 7:31
 */
public class RpcSerializer {
    // TODO 动态加载类加载器
    private static Serializer serializer = new HessianSerializer();

    /**
     * 序列化对象为byte[]
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        return serializer.serialize(object);
    }

    /**
     * 序列化byte[]为对象
     * @param bytes
     * @param clazz 目标对象类型
     * @return
     * @param <T>
     */
    public static <T> T deserialize(byte[] bytes, Class<T> clazz){
        return serializer.deserialize(bytes, clazz);
    }
}
