package com.qyling.qRPC_simple.config;

import com.qyling.qRPC_simple.serialize.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * RPC配置实体（从config.properties中读取）
 * @author qyling
 * @date 2024/11/3 10:25
 */
@Data
@Slf4j
public class RpcConfig {
    private String host = RpcConstant.DEFAULT_HOST;
    private Integer port = RpcConstant.DEFAULT_PORT;
    private Boolean mock = false;
    private String serializer = "com.qyling.qRPC_simple.serialize.JDKSerializer";
    private Serializer serializerObj = null;
    private Byte serializationID = null;

    public String getUrl() {
        return host + ":" + port;
    }

    public Serializer getSerializerObj() {
        if (serializerObj == null) {
            synchronized (this) {
                if (serializerObj == null) {
                    try {
                        Class<?> clazz = Class.forName(serializer);
                        Class<?>[] interfaces = clazz.getInterfaces();
                        // 是否实现 Serializable
                        boolean result = false;
                        for (Class<?> anInterface : interfaces) {
                            if (anInterface.getName().equals(Serializer.class.getName())) {
                                result = true;
                            }
                        }
                        if (!result) {
                            log.error("指定的序列化器未实现接口：{}", Serializer.class.getName());
                            throw new RuntimeException();
                        }
                        // 反射构造实例
                        serializerObj = (Serializer) clazz.getDeclaredConstructor().newInstance();
                    } catch (ClassNotFoundException e) {
                        log.error("未找到序列化器：{}", serializer);
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                             InstantiationException e) {
                        log.error("序列化器初始化失败");
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return serializerObj;
    }

    public Byte getSerializationID() {
        if (serializationID == null) {
            synchronized (this) {
                if (serializationID == null) {
                    Class<? extends Serializer> clazz = getSerializerObj().getClass();
                    if (clazz == JDKSerializer.class) {
                        serializationID =  SerializationEnum.JDK.getSerializationID();
                    } else if (clazz == HessianSerializer.class) {
                        serializationID =  SerializationEnum.HESSIAN.getSerializationID();
                    } else if (clazz == KryoSerializer.class) {
                        serializationID =  SerializationEnum.KRYO.getSerializationID();
                    } else if (clazz == JSONSerializer.class) {
                        serializationID =  SerializationEnum.JSON.getSerializationID();
                    } else {
                        serializationID =  SerializationEnum.OTHER.getSerializationID();
                    }
                }
            }
        }
        return serializationID;
    }
}
