package com.qyling.qRPC.serialize;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author qyling
 * @date 2024/11/5 8:55
 */
public class HessianSerializer implements Serializer{
    @Override
    public byte[] serialize(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(bos);
        try {
            ho.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        HessianInput hi = new HessianInput(bis);
        try {
            return (T) hi.readObject(clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
