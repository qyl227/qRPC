package com.qyling.qRPC_simple.config;

import com.qyling.qRPC_simple.retry.RetryStrategy;
import com.qyling.qRPC_simple.serialize.*;
import com.qyling.qRPC_simple.utils.SingletonUtils;
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
    private String retryStrategy = "com.qyling.qRPC_simple.retry.FixedWaitStrategy";
    private volatile RetryStrategy retryStrategyObj = null;
    private String serializer = "com.qyling.qRPC_simple.serialize.JDKSerializer";
    private volatile Serializer serializerObj = null;
    private Byte serializationID = null;

    public String getUrl() {
        return host + ":" + port;
    }

    /**
     * 单例模式
     * @return 序列化器对象
     */
    public Serializer getSerializerObj() {
        return SingletonUtils.getSingleton(serializerObj, serializer, Serializer.class);
    }

    public RetryStrategy getRetryStrategyObj() {
        return SingletonUtils.getSingleton(retryStrategyObj, retryStrategy, RetryStrategy.class);
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
