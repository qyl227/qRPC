package com.qyling.qRPC.serialize;

/**
 * @author qyling
 * @date 2024/11/3 18:54
 */
public interface Serializer {
    public byte[] serialize(Object object);
    public <T> T deserialize(byte[] bytes, Class<T> clazz);
}
