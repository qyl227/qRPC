package com.qyling.qRPC_simple.serialize;

import java.io.*;

/**
 * 原生JDK实现的序列化器（默认）
 * @author qyling
 * @date 2024/11/3 19:32
 */
public class JDKSerializer implements Serializer {
    @Override
    public byte[] serialize(Object object) {
        // 序列化到字节数组
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(byteOut);
            out.writeObject(object);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return byteOut.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
        ObjectInputStream in = null;
        Object object = null;
        try {
            in = new ObjectInputStream(byteIn);
            object = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return (T) object;
    }
}
