package com.qyling.qRPC_simple.serialize;

import java.io.*;

/**
 * 原生JDK实现的序列化器
 * @author qyling
 * @date 2024/10/31 7:31
 */
public class RpcSerializer {
    /**
     * 序列化对象为byte[]
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
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

    /**
     * 序列化byte[]为对象
     * @param bytes
     * @return
     * @param <T>
     */
    public static <T> T deserialize(byte[] bytes){
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
